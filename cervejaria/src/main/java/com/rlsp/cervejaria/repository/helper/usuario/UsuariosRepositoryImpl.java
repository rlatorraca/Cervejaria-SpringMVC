package com.rlsp.cervejaria.repository.helper.usuario;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.rlsp.cervejaria.model.Grupo;
import com.rlsp.cervejaria.model.Usuario;
import com.rlsp.cervejaria.model.UsuarioGrupo;
import com.rlsp.cervejaria.repository.filter.UsuarioFilter;
import com.rlsp.cervejaria.repository.paginacao.PaginacaoUtil;

public class UsuariosRepositoryImpl implements UsuariosRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@Override
	public Optional<Usuario> porEmailEAtivo(String email) {
		return manager
				.createQuery("from Usuario where lower(email) = lower(:email) and ativo = true", Usuario.class)
				.setParameter("email", email)
					.getResultList()
					.stream().findFirst(); // Retorna um Optional vazio se NAO ACHAR nada
	}

	@Override
	public List<String> permissoes(Usuario usuario) {
		return manager.createQuery(
				"select distinct p.nome from Usuario u inner join u.grupos g inner join g.permissoes p where u = :usuario", String.class)
				.setParameter("usuario", usuario)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Usuario.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		//criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		adicionarFiltro(filtro, criteria);
		
		List<Usuario> filtrados = criteria.list();
		
		filtrados.forEach(u -> Hibernate.initialize(u.getGrupos())); // Pega os Grupos

		return new PageImpl<>(filtrados, pageable, total(filtro));
	}
	
	private Long total(UsuarioFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Usuario.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(UsuarioFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
			
			if (!StringUtils.isEmpty(filtro.getEmail())) {
				criteria.add(Restrictions.ilike("email", filtro.getEmail(), MatchMode.START));
			}
			
			//criteria.createAlias("grupos", "g", JoinType.LEFT_OUTER_JOIN);
			
			if (filtro.getGrupos() != null && !filtro.getGrupos().isEmpty()) {
				
				List<Criterion> subqueries = new ArrayList<>();
				
				/**
				 * .stream() ==> mapeia em STREAM (iterador)
				 * .mapToLong(Grupo::getCodigo) ==> transforma em um LONG, o atributi "codigo" na Entidade model/Grupo
				 */
				for (Long codigoGrupo : filtro.getGrupos().stream().mapToLong(Grupo::getCodigo).toArray()) {
					
					/**
					 * Criteria/Consultada DESTACADA/SEPARADA
					 *  - Sera chamanda PRIMEIRO, antes da principal
					 */
					DetachedCriteria dc = DetachedCriteria.forClass(UsuarioGrupo.class);
					dc.add(Restrictions.eq("id.grupo.codigo", codigoGrupo));  // Filtra por "*"
					
					dc.setProjection(Projections.property("id.usuario"));	  //Retorna o Código do Usuario					
					subqueries.add(Subqueries.propertyIn("codigo", dc));
				}
				
				Criterion[] criterions = new Criterion[subqueries.size()];
				criteria.add(Restrictions.and(subqueries.toArray(criterions)));
				
			}
		}
	}

	@Transactional(readOnly = true) // readOnly = apenas para pesquisa
	@Override
	public Usuario buscarComGrupos(Long codigo) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Usuario.class);
		criteria.createAlias("grupos", "g", JoinType.LEFT_OUTER_JOIN); // traz os grupos junto com cada USUARIO
		criteria.add(Restrictions.eq("codigo", codigo)); // Filtrado pelo CODIGO
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // Agrupar por USUARIO
		return (Usuario) criteria.uniqueResult();
	}

	

}
