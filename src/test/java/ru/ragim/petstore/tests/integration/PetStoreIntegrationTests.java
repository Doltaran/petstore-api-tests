package ru.ragim.petstore.tests.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.ragim.petstore.config.AbstractIntegrationTest;
import ru.ragim.petstore.data.TestDataFactory;
import ru.ragim.petstore.steps.PetSteps;
import ru.ragim.petstore.steps.StoreSteps;

@Tag("regression")
public class PetStoreIntegrationTests extends AbstractIntegrationTest {

    private final PetSteps pet = new PetSteps();
    private final StoreSteps store = new StoreSteps();

    @Test
    @DisplayName("INTEGRATION: Complex flow with multiple operations")
    void complexIntegrationFlow() {
        // Этот тест уже включен в StoreTests, но оставляем для дополнительных интеграционных сценариев
        long petId = TestDataFactory.uniqueId();
        long orderId = TestDataFactory.uniqueId();

        // Create pet
        pet.createPet(TestDataFactory.pet(petId, "IntegrationPet", "available"));
        pet.assertPetExists(petId, "IntegrationPet");

        // Create order for petId
        store.createOrder(TestDataFactory.order(orderId, petId, 1, "placed"));
        store.assertOrderExists(orderId);

        // Cleanup
        store.deleteOrder(orderId);
        pet.deletePet(petId);
        pet.assertPetDeleted(petId);
    }
}
