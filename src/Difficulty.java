public enum Difficulty {
    EASY,
    NORMAL,
    HARD;
	
	//This method gives back the first two letters of the difficulty and reverses the capitals to lower cases.
    public String getName(){
        String lowerCase = this.toString().toLowerCase();
        return lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
    }
}
