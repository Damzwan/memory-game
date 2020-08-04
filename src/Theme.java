public enum Theme {
    ANIMALS,
    COFFEE;

    public String getName(){
        String lowerCase = this.toString().toLowerCase();
        return lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
    }
}
