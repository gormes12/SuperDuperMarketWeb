package utils;

public class ThreadSafeUtils {
    public static final Object orderManagerLock = new Object();
    public static final Object feedbackManagerLock = new Object();
    public static final Object chatManagerLock = new Object();
    public static final Object zonesManagerLock = new Object();
    public static final Object storeManagerLock = new Object();
    public static final Object itemManagerLock = new Object();

}
