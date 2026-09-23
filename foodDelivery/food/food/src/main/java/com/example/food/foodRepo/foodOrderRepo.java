package com.example.food.foodRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.food.food.model.foodOrder;
import com.example.food.food.model.orderStatus;

@Repository
public interface foodOrderRepo extends JpaRepository<foodOrder, Integer> {

	@Query("SELECT o FROM foodOrder o WHERE o.deliverypartner.id = :id")
	List<foodOrder> findDeliveryPartnerid(@Param("id") int id);

	List<foodOrder> findByDeliverypartnerId(int partnerId);

	List<foodOrder> findByCustomerId(int customerId);

	List<foodOrder> findByDeliverypartnerIdAndStatusIn(int partnerId, List<orderStatus> statuses);

	List<foodOrder> findByStatus(orderStatus status);
}
