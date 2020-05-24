package com.rlsp.cervejaria.repository.helper.cliente;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.rlsp.cervejaria.dto.CervejaDTO;
import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.model.Cliente;
import com.rlsp.cervejaria.repository.filter.CervejaFilter;
import com.rlsp.cervejaria.repository.filter.ClienteFilter;
import com.rlsp.cervejaria.repository.paginacao.PaginacaoUtil;

public class ClientesRepositoryImpl  implements ClientesRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)	
	public Page<Cliente> filtrar(ClienteFilter filtro, Pageable pageable) {
		//public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);
		Root<Cliente> clienteEntity = query.from(Cliente.class);
		Predicate[] filtros = adicionarFiltro(filtro, clienteEntity);

		query.select(clienteEntity).where(filtros);
		
		TypedQuery<Cliente> typedQuery =  (TypedQuery<Cliente>) paginacaoUtil.prepararOrdem(query, clienteEntity, pageable);
		typedQuery = (TypedQuery<Cliente>) paginacaoUtil.prepararIntervalo(typedQuery, pageable);
		
		return new PageImpl<>(typedQuery.getResultList(), pageable, total(filtro));
	}
	
	private Long total(ClienteFilter filtro) {
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		Root<Cliente> clienteEntity = query.from(Cliente.class);
		
		query.select(criteriaBuilder.count(clienteEntity));   // Conta a quantidade de clientes no DB
		query.where(adicionarFiltro(filtro, clienteEntity));
		
		return manager.createQuery(query).getSingleResult();
	}
	
	private Predicate[] adicionarFiltro(ClienteFilter filtro, Root<Cliente> clienteEntity) {
		List<Predicate> predicateList = new ArrayList<>();
		CriteriaBuilder builder = manager.getCriteriaBuilder();

		
		if (filtro != null) {
						
			if (!StringUtils.isEmpty(filtro.getNome())) {
				predicateList.add(builder.like(clienteEntity.get("nome"), "%" + filtro.getNome() + "%"));
			}
			
			if (!StringUtils.isEmpty(filtro.getCpfOuCnpj())) {
				predicateList.add(builder.equal(clienteEntity.get("cpfOuCnpj"), filtro.getCpfOuCnpjSemFormatacao()));
			}

		}
		
		Predicate[] predArray = new Predicate[predicateList.size()];
		return predicateList.toArray(predArray);
	}

	@SuppressWarnings("unused")
	private void adicionarFiltroAntigo(CervejaFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getSku())) {
				criteria.add(Restrictions.eq("sku", filtro.getSku()));
			}
			
			if (!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}

			if (isEstiloPresente(filtro)) {
				criteria.add(Restrictions.eq("estilo", filtro.getEstilo()));
			}

			if (filtro.getSabor() != null) {
				criteria.add(Restrictions.eq("sabor", filtro.getSabor()));
			}

			if (filtro.getOrigem() != null) {
				criteria.add(Restrictions.eq("origem", filtro.getOrigem()));
			}

			if (filtro.getValorDe() != null) {
				criteria.add(Restrictions.ge("valor", filtro.getValorDe()));
			}

			if (filtro.getValorAte() != null) {
				criteria.add(Restrictions.le("valor", filtro.getValorAte()));
			}
		}
	}
	
	private boolean isEstiloPresente(CervejaFilter filtro) {
		return filtro.getEstilo() != null && filtro.getEstilo().getCodigo() != null;
	}

	public List<CervejaDTO> porSkuOuNome(String skuOuNome) {
		
		String jpql = "select new com.algaworks.brewer.dto.CervejaDTO(codigo, sku, nome, origem, valor, foto) "
				+ "from Cerveja where lower(sku) like lower(:skuOuNome) or lower(nome) like lower(:skuOuNome)";
		
		List<CervejaDTO> cervejasFiltradas = manager.createQuery(jpql, CervejaDTO.class)
					.setParameter("skuOuNome", skuOuNome + "%")
					.getResultList();
		
		return cervejasFiltradas;
	}
}