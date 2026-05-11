class SaveStorage {
    private static SaveStorage instance = new SaveStorage();
    int bestScore = 0;

    private SaveStorage() {
    }

    static SaveStorage get() {
        return instance;
    }
}
