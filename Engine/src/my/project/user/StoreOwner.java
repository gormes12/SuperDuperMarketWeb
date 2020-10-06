package my.project.user;

public class StoreOwner extends User{

    public StoreOwner(int id, String username) {
        super(id, username, eUserType.StoreOwner);
    }

    @Override
    public eUserType getUserType() {
        return eUserType.StoreOwner;
    }
}
