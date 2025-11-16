package sprint4.mode;

public enum GameMode4 {
	SIMPLE("Simple Game"),
	GENERAL("General Game");
	
	private  String name;
	
	GameMode4(String name)
	{
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
