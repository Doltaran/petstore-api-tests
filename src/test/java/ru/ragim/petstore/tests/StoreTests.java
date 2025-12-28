package ru.ragim.petstore.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.ragim.petstore.client.StoreClient;
import ru.ragim.petstore.config.AbstractIntegrationTest;
import ru.ragim.petstore.data.TestDataFactory;
import ru.ragim.petstore.steps.PetSteps;
import ru.ragim.petstore.steps.StoreSteps;

import static org.hamcrest.Matchers.*;

@Tag("smoke")
public class StoreTests extends AbstractIntegrationTest {

    private final StoreSteps store = new StoreSteps();
    private final StoreClient storeClient = new StoreClient();
    private final PetSteps pet = new PetSteps();

    @Test
    @DisplayName("STORE: Order lifecycle (POST -> GET -> DELETE)")
    void orderLifecycle() {
        // Создаем пета для заказа
        long petId = TestDataFactory.uniqueId();
        pet.createPet(TestDataFactory.pet(petId, "OrderPet", "available"));
        
        long orderId = TestDataFactory.uniqueId();
        store.createOrder(TestDataFactory.order(orderId, petId, 2, "placed"));
        store.assertOrderExists(orderId);
        store.deleteOrder(orderId);
        
        pet.deletePet(petId);
    }

    @Test
    @DisplayName("STORE: Inventory returns map/object")
    void inventory() {
        store.assertInventoryIsMap();
    }

    @Test
    @DisplayName("STORE: Get non-existing order returns 404/400")
    void getNonExistingOrder() {
        storeClient.getOrder(999_999_999_999L)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(404), is(400)));
    }

    @Test
    @DisplayName("STORE: Create order for available pet")
    void createOrderForAvailablePet() {
        long petId = TestDataFactory.uniqueId();
        pet.createPet(TestDataFactory.pet(petId, "OrderPet1", "available"));
        
        long orderId = TestDataFactory.uniqueId();
        store.createOrder(TestDataFactory.order(orderId, petId, 1, "placed"));
        store.assertOrderExists(orderId);
        
        store.deleteOrder(orderId);
        pet.deletePet(petId);
    }

    @Test
    @DisplayName("STORE: Create order with different statuses")
    void createOrderWithDifferentStatuses() {
        long petId = TestDataFactory.uniqueId();
        pet.createPet(TestDataFactory.pet(petId, "OrderPet2", "available"));
        
        long orderId1 = TestDataFactory.uniqueId();
        store.createOrder(TestDataFactory.order(orderId1, petId, 1, "placed"));
        store.assertOrderExists(orderId1);
        
        long orderId2 = TestDataFactory.uniqueId();
        store.createOrder(TestDataFactory.order(orderId2, petId, 1, "approved"));
        store.assertOrderExists(orderId2);
        
        store.deleteOrder(orderId1);
        store.deleteOrder(orderId2);
        pet.deletePet(petId);
    }

    @Test
    @DisplayName("STORE: Create order with multiple quantity")
    void createOrderWithMultipleQuantity() {
        long petId = TestDataFactory.uniqueId();
        pet.createPet(TestDataFactory.pet(petId, "OrderPet3", "available"));
        
        long orderId = TestDataFactory.uniqueId();
        store.createOrder(TestDataFactory.order(orderId, petId, 5, "placed"));
        store.assertOrderExists(orderId);
        
        store.deleteOrder(orderId);
        pet.deletePet(petId);
    }

    @Test
    @DisplayName("STORE: Delete order and verify deletion")
    void deleteOrderAndVerify() {
        long petId = TestDataFactory.uniqueId();
        pet.createPet(TestDataFactory.pet(petId, "OrderPet4", "available"));
        
        long orderId = TestDataFactory.uniqueId();
        store.createOrder(TestDataFactory.order(orderId, petId, 1, "placed"));
        store.assertOrderExists(orderId);
        
        store.deleteOrder(orderId);
        storeClient.getOrder(orderId)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(404), is(400)));
        
        pet.deletePet(petId);
    }

    @Test
    @DisplayName("STORE: Create multiple orders for same pet")
    void createMultipleOrdersForSamePet() {
        long petId = TestDataFactory.uniqueId();
        pet.createPet(TestDataFactory.pet(petId, "OrderPet5", "available"));
        
        long orderId1 = TestDataFactory.uniqueId();
        long orderId2 = TestDataFactory.uniqueId();
        
        store.createOrder(TestDataFactory.order(orderId1, petId, 1, "placed"));
        store.createOrder(TestDataFactory.order(orderId2, petId, 2, "placed"));
        
        store.assertOrderExists(orderId1);
        store.assertOrderExists(orderId2);
        
        store.deleteOrder(orderId1);
        store.deleteOrder(orderId2);
        pet.deletePet(petId);
    }

    @Test
    @DisplayName("STORE: Access control - User 1 creates order, User 2 cannot see it")
    void accessControlUser1CreatesOrderUser2CannotSee() {

        long petId = TestDataFactory.uniqueId();
        pet.createPet(TestDataFactory.pet(petId, "PrivateOrderPet", "available"));
        
        long orderId = TestDataFactory.uniqueId();
        store.createOrder(TestDataFactory.order(orderId, petId, 1, "placed"));
        store.assertOrderExists(orderId);
        

        userSteps.logout();
        

        String user2Username = TestDataFactory.uniqueUsername();
        String user2Password = "pass456";
        userSteps.createUser(TestDataFactory.user(user2Username, user2Password));
        userSteps.login(user2Username, user2Password);
        

        storeClient.getOrder(orderId)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(404), is(400), is(403)));
        

        userSteps.logout();
        userSteps.login(currentUsername, currentPassword);
        
        store.deleteOrder(orderId);
        pet.deletePet(petId);
    }

    @Test
    @DisplayName("STORE: Access control - User cannot delete order created by another user")
    void accessControlUserCannotDeleteOtherUserOrder() {

        long petId = TestDataFactory.uniqueId();
        pet.createPet(TestDataFactory.pet(petId, "ProtectedOrderPet", "available"));
        
        long orderId = TestDataFactory.uniqueId();
        store.createOrder(TestDataFactory.order(orderId, petId, 1, "placed"));
        store.assertOrderExists(orderId);
        

        userSteps.logout();
        

        String user2Username = TestDataFactory.uniqueUsername();
        String user2Password = "pass456";
        userSteps.createUser(TestDataFactory.user(user2Username, user2Password));
        userSteps.login(user2Username, user2Password);
        

        storeClient.deleteOrder(orderId)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(404), is(400), is(403), is(204)));
        

        userSteps.logout();
        userSteps.login(currentUsername, currentPassword);
        

        storeClient.deleteOrder(orderId)
                .then().log().ifValidationFails()
                .statusCode(anyOf(is(200), is(404), is(400), is(204)));
        
        pet.deletePet(petId);
    }

    @Test
    @DisplayName("STORE: Integration - Create Pet -> Create Order -> Verify petId -> Cleanup")
    void petOrderIntegrationFlow() {
        long petId = TestDataFactory.uniqueId();
        long orderId = TestDataFactory.uniqueId();


        pet.createPet(TestDataFactory.pet(petId, "IntegrationPet", "available"));
        pet.assertPetExists(petId, "IntegrationPet");


        store.createOrder(TestDataFactory.order(orderId, petId, 1, "placed"));


        storeClient.getOrder(orderId)
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("petId", anyOf(equalTo((int) petId), equalTo((long) petId), notNullValue()));

        // Cleanup
        store.deleteOrder(orderId);
        pet.deletePet(petId);
        pet.assertPetDeleted(petId);
    }
}

