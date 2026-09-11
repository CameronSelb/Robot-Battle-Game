package server.configuration;

public class Config {

    private int port;
    private int height;
    private int width;
    private int shields;
    private int shots;
    private int reloadTime;
    private int repairTime;
    private int lookDistance;
    private int mineActivation;
    private int numberOfObstacles;

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getShields() { return shields; }
    public void setShields(int shields) { this.shields = shields; }

    public int getShots() { return shots; }
    public void setShots(int shots) { this.shots = shots; }

    public int getReloadTime() { return reloadTime; }
    public void setReloadTime(int reloadTime) { this.reloadTime = reloadTime; }

    public int getRepairTime() { return repairTime; }
    public void setRepairTime(int repairTime) { this.repairTime = repairTime; }

    public int getLookDistance() { return lookDistance; }
    public void setLookDistance(int lookDistance) { this.lookDistance = lookDistance; }

    public int getMineActivation() { return mineActivation; }
    public void setMineActivation(int mineActivation) { this.mineActivation = mineActivation; }

    public int getNumberOfObstacles() { return numberOfObstacles; }
    public void setNumberOfObstacles(int numberOfObstacles) { this.numberOfObstacles = numberOfObstacles; }
}