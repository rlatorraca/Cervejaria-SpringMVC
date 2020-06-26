package com.rlsp.cervejaria.repository.helper.cerveja;

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
import com.rlsp.cervejaria.dto.ValorItensEstoque;
import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.repository.filter.CervejaFilter;
import com.rlsp.cervejaria.repository.paginacao.PaginacaoUtil;
import com.rlsp.cervejaria.storage.FotoStorage;

public class CervejasRepositoryImpl  implements CervejasRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)	
	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {
		//public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Cerveja> query = builder.createQuery(Cerveja.class);
		Root<Cerveja> cervejaEntity = query.from(Cerveja.class);
		Predicate[] filtros = adicionarFiltro(filtro, cervejaEntity);

		query.select(cervejaEntity).where(filtros);
		
		TypedQuery<Cerveja> typedQuery =  (TypedQuery<Cerveja>) paginacaoUtil.prepararOrdem(query, cervejaEntity, pageable);
		typedQuery = (TypedQuery<Cerveja>) paginacaoUtil.prepararIntervalo(typedQuery, pageable);
		
		return new PageImpl<>(typedQuery.getResultList(), pageable, total(filtro));
	}
	
	private Long total(CervejaFilter filtro) {
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		Root<Cerveja> cervejaEntity = query.from(Cerveja.class);
		
		query.select(criteriaBuilder.count(cervejaEntity));   // Conta a quantidade de cervejas no DB
		query.where(adicionarFiltro(filtro, cervejaEntity));
		
		return manager.createQuery(query).getSingleResult();
	}
	
	private Predicate[] adicionarFiltro(CervejaFilter filtro, Root<Cerveja> cervejaEntity) {
		List<Predicate> predicateList = new ArrayList<>();
		CriteriaBuilder builder = manager.getCriteriaBuilder();

		
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getSku())) {
				predicateList.add(builder.equal(cervejaEntity.get("sku"), filtro.getSku()));
			}
			
			if (!StringUtils.isEmpty(filtro.getNome())) {
				predicateList.add(builder.like(cervejaEntity.get("nome"), "%" + filtro.getNome() + "%"));
			}

			if (isEstiloPresente(filtro)) {
				predicateList.add(builder.equal(cervejaEntity.get("estilo"), filtro.getEstilo()));
			}

			if (filtro.getSabor() != null) {
				predicateList.add(builder.equal(cervejaEntity.get("sabor"), filtro.getSabor()));
			}

			if (filtro.getOrigem() != null) {
				predicateList.add(builder.equal(cervejaEntity.get("origem"), filtro.getOrigem()));
			}

			if (filtro.getValorDe() != null) {
				predicateList.add(builder.ge(cervejaEntity.get("valor"), filtro.getValorDe()));
			}

			if (filtro.getValorAte() != null) {
				predicateList.add(builder.le(cervejaEntity.get("valor"), filtro.getValorAte()));
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

	
	/**
	 * Usado para mostra a CERVEJA com FOTO na pagina de VENDAS / PEDIDOS	 * 
	 */
	
	public List<CervejaDTO> porSkuOuNome(String skuOuNome) {
		
		String jpql = "select new com.rlsp.cervejaria.dto.CervejaDTO(codigo, sku, nome, origem, valor, foto) "
				+ "from Cerveja where lower(sku) like lower(:skuOuNome) or lower(nome) like lower(:skuOuNome)";
		
		List<CervejaDTO> cervejasFiltradas = manager.createQuery(jpql, CervejaDTO.class)
					.setParameter("skuOuNome", "%" + skuOuNome + "%")
					.getResultList();
				
		cervejasFiltradas.forEach(c -> c.setUrlThumbnailFoto(fotoStorage.getUrl(FotoStorage.THUMBNAIL_PREFIX + c.getFoto())));
		
		return cervejasFiltradas;
	}
	
	@Override
	public ValorItensEstoque valorItensEstoque() {
		String query = "select new com.rlsp.cervejaria.dto.ValorItensEstoque(sum(valor * quantidadeEstoque), sum(quantidadeEstoque)) from Cerveja";
		return manager.createQuery(query, ValorItensEstoque.class).getSingleResult();
	}
}