package com.rlsp.cervejaria.repository.helper.cidade;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
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

import com.rlsp.cervejaria.model.Cidade;
import com.rlsp.cervejaria.model.Cliente;
import com.rlsp.cervejaria.model.Estado;
import com.rlsp.cervejaria.repository.filter.CervejaFilter;
import com.rlsp.cervejaria.repository.filter.CidadeFilter;
import com.rlsp.cervejaria.repository.paginacao.PaginacaoUtil;

public class CidadesRepositoryImpl implements CidadesRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)	
	public Page<Cidade> filtrar(CidadeFilter filtro, Pageable pageable) {
		//public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Cidade> query = builder.createQuery(Cidade.class);
		Root<Cidade> cidadeEntity = query.from(Cidade.class);
		
		
		Fetch<Cidade, Estado> estadoCliente = cidadeEntity.fetch("estado",JoinType.LEFT);
		
		
		Predicate[] filtros = adicionarFiltro(filtro, cidadeEntity);

		query.select(cidadeEntity).where(filtros);
		
		TypedQuery<Cidade> typedQuery =  (TypedQuery<Cidade>) paginacaoUtil.prepararOrdem(query, cidadeEntity, pageable);
		typedQuery = (TypedQuery<Cidade>) paginacaoUtil.prepararIntervalo(typedQuery, pageable);
		
		return new PageImpl<>(typedQuery.getResultList(), pageable, total(filtro));
	}
	
	private Long total(CidadeFilter filtro) {
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		Root<Cidade> cidadeEntity = query.from(Cidade.class);
		
		query.select(criteriaBuilder.count(cidadeEntity));   // Conta a quantidade de cervejas no DB
		query.where(adicionarFiltro(filtro, cidadeEntity));
		
		return manager.createQuery(query).getSingleResult();
	}
	
	private Predicate[] adicionarFiltro(CidadeFilter filtro, Root<Cidade> cidadeEntity) {
		List<Predicate> predicateList = new ArrayList<>();
		CriteriaBuilder builder = manager.getCriteriaBuilder();

		
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getEstado())) {
				predicateList.add(builder.equal(cidadeEntity.get("estado"), filtro.getEstado()));
			}
			
			if (!StringUtils.isEmpty(filtro.getNome())) {
				predicateList.add(builder.like(cidadeEntity.get("nome"), "%" + filtro.getNome() + "%"));
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

			
		}
	}
}
