package com.gespyme.infrastructure.adapters.output.repository;

import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import com.gespyme.infrastructure.adapters.input.rest.customer.CustomerApiRestCallService;
import com.gespyme.infrastructure.adapters.output.model.entities.InvoiceDataEntity;
import com.gespyme.infrastructure.adapters.output.model.entities.QInvoiceDataEntity;
import com.gespyme.infrastructure.adapters.output.model.entities.QInvoiceOrderEntity;
import com.gespyme.infrastructure.adapters.output.repository.jpa.InvoiceDataRepositorySpringJpa;
import com.gespyme.infrastructure.mapper.InvoiceDataMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class InvoiceDataRepositoryImpl implements InvoiceDataRepository {

  private final Map<String, QueryField> queryFieldMap;
  private final InvoiceDataRepositorySpringJpa invoiceDataRepositorySpringJpa;
  private final JPAQueryFactory queryFactory;
  private final InvoiceDataMapper mapper;

  public InvoiceDataRepositoryImpl(
      List<QueryField> queryFields,
      InvoiceDataRepositorySpringJpa invoiceDataRepositorySpringJpa,
      JPAQueryFactory queryFactory,
      InvoiceDataMapper mapper,
      CustomerApiRestCallService customerApiRestCallService) {
    this.invoiceDataRepositorySpringJpa = invoiceDataRepositorySpringJpa;
    this.queryFactory = queryFactory;
    this.mapper = mapper;
    queryFieldMap =
        queryFields.stream()
            .collect(Collectors.toMap(QueryField::getFieldName, queryField -> queryField));
  }

  @Override
  public List<InvoiceData> findByCriteria(List<SearchCriteria> searchCriteria) {
    QInvoiceDataEntity invoiceData = QInvoiceDataEntity.invoiceDataEntity;
    QInvoiceOrderEntity invoiceOrder = QInvoiceOrderEntity.invoiceOrderEntity;

    JPAQuery<InvoiceDataEntity> query =
        queryFactory
            .select(invoiceData)
            .from(invoiceData)
            .join(invoiceData.invoiceOrder, invoiceOrder);
    BooleanBuilder booleanBuilder = getPredicates(searchCriteria);
    List<Tuple> result =
        Objects.nonNull(booleanBuilder.getValue())
            ? executeQueryWithPredicate(invoiceData, invoiceOrder, booleanBuilder, query)
            : executeQueryWithoutPredicate(invoiceData, invoiceOrder, query);
    return result.stream()
        .map(tuple -> mapTuple(tuple, invoiceData, invoiceOrder))
        .collect(Collectors.toList());
  }

  private BooleanBuilder getPredicates(List<SearchCriteria> searchCriteria) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    searchCriteria.stream()
        .forEach(sc -> queryFieldMap.get(sc.getKey()).addToQuery(booleanBuilder, sc));
    return booleanBuilder;
  }

  private List<Tuple> executeQueryWithPredicate(
      QInvoiceDataEntity invoiceData,
      QInvoiceOrderEntity invoiceOrder,
      BooleanBuilder booleanBuilder,
      JPAQuery<InvoiceDataEntity> query) {
    return query
        .select(
            invoiceData.subtotalAmount,
            invoiceData.taxRate,
            invoiceData.totalAmount,
            invoiceData.description,
            invoiceOrder.status)
        .where(booleanBuilder)
        .fetch();
  }

  private List<Tuple> executeQueryWithoutPredicate(
      QInvoiceDataEntity invoiceData,
      QInvoiceOrderEntity invoiceOrder,
      JPAQuery<InvoiceDataEntity> query) {
    return query
        .select(
            invoiceData.subtotalAmount,
            invoiceData.taxRate,
            invoiceData.totalAmount,
            invoiceData.description,
            invoiceOrder.status)
        .fetch();
  }

  private InvoiceData mapTuple(
      Tuple tuple, QInvoiceDataEntity invoiceData, QInvoiceOrderEntity invoiceOrder) {
    return InvoiceData.builder()
        .subtotalAmount(tuple.get(invoiceData.subtotalAmount))
        .totalAmount(tuple.get(invoiceData.totalAmount))
        .description(tuple.get(invoiceData.description))
        .taxRate(tuple.get(invoiceData.taxRate))
        .status(tuple.get(invoiceOrder.status))
        .build();
  }

  @Override
  public Optional<InvoiceData> findById(String id) {
    return invoiceDataRepositorySpringJpa.findById(id).map(mapper::map);
  }

  @Override
  public void deleteById(String id) {
    invoiceDataRepositorySpringJpa.deleteById(id);
  }

  @Override
  public InvoiceData save(InvoiceData invoiceData) {
    InvoiceDataEntity invoiceDataEntity =
        invoiceDataRepositorySpringJpa.save(mapper.mapToEntity(invoiceData));
    return mapper.map(invoiceDataEntity);
  }

  @Override
  public InvoiceData merge(InvoiceData newInvoiceDataData, InvoiceData invoiceData) {
    InvoiceData merged = mapper.merge(newInvoiceDataData, invoiceData);
    InvoiceDataEntity savedEntity = invoiceDataRepositorySpringJpa.save(mapper.mapToEntity(merged));
    return mapper.map(savedEntity);
  }
}
