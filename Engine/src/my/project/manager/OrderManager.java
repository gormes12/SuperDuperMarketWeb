package my.project.manager;

import dto.OrderDTO;
import dto.ShoppingCartDTO;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private List<ShoppingCartDTO> shoppingCartList = new ArrayList<>();

    public synchronized void addOrder(ShoppingCartDTO shoppingCart){
        shoppingCartList.add(shoppingCart);
    }

    public synchronized List<ShoppingCartDTO> getOrdersEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > shoppingCartList.size()) {
            fromIndex = 0;
        }
        return shoppingCartList.subList(fromIndex, shoppingCartList.size());
    }

    public int getVersion() {
        return shoppingCartList.size();
    }
}
