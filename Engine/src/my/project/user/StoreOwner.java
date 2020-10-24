package my.project.user;

import dto.OrderDTO;
import dto.ShoppingCartDTO;
import dto.StoreDTO;
import dto.ZoneDTO;
import feedback.Feedback;
import my.project.manager.FeedbackManager;
import my.project.manager.OrderManager;
import my.project.order.Order;

import java.time.LocalDate;
import java.util.*;

public class StoreOwner extends User{

    private final OrderManager orderManager;
    private final FeedbackManager feedbacksManager;
    private final List<StoreDTO> competitiveStoresInMyZone;

    public StoreOwner(int id, String username) {
        super(id, username, eUserType.StoreOwner);
        feedbacksManager = new FeedbackManager();
        orderManager = new OrderManager();
        competitiveStoresInMyZone = new ArrayList<>();
    }

    @Override
    public eUserType getUserType() {
        return eUserType.StoreOwner;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public FeedbackManager getFeedbacksManager() {
        return feedbacksManager;
    }

    public synchronized void addCompetitiveStore(StoreDTO store){
        competitiveStoresInMyZone.add(store);
    }

    public synchronized List<StoreDTO> getCompetitiveStoreEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > competitiveStoresInMyZone.size()) {
            fromIndex = 0;
        }
        return competitiveStoresInMyZone.subList(fromIndex, competitiveStoresInMyZone.size());
    }

    public int getCompetitiveStoresVersion() {
        return competitiveStoresInMyZone.size();
    }
}
