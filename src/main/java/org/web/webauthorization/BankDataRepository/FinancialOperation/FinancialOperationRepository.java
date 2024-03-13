package org.web.webauthorization.BankDataRepository.FinancialOperation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.web.webauthorization.BankData.FinancialOperation.FinancialOperation;

import java.util.List;
import java.util.UUID;

public interface FinancialOperationRepository extends JpaRepository<FinancialOperation, Long> {

    @Query("SELECT fo FROM FinancialOperation fo WHERE fo.senderId = :senderId OR fo.recipientId = :recipientId OR fo.operationCreatorId = :operationCreatorId")
    List<FinancialOperation> findBySenderIdOrRecipientIdOrOperationCreatorId(@Param("senderId") Long senderId, @Param("recipientId") Long recipientId, @Param("operationCreatorId") Long operationCreatorId);

   
    List<FinancialOperation> findByOperationCreatorId(Long operationCreatorId);
}

