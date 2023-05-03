package game;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class Player extends AbstractCharacter
{

    private final static int PLAYER_START_X = 60;
    private final static int PLAYER_START_Y = 60;
    private final static int PLAYER_PIXELS_BY_STEP = 4;
    private int explosionRadius;
    private int bombCount;
    private Floor floor;
 
    public Action up = new AbstractAction() {
	public void actionPerformed(ActionEvent e) {
	    movePlayer(Move.UP);

	}
    };
 
    public Action right = new AbstractAction() {
	public void actionPerformed(ActionEvent e) {
	    movePlayer(Move.RIGHT);

	}
    };
   
    public Action down = new AbstractAction() {
	public void actionPerformed(ActionEvent e) {
	    movePlayer(Move.DOWN);

	}
    };
  
    public Action left = new AbstractAction() {
	public void actionPerformed(ActionEvent e) {
	    movePlayer(Move.LEFT);

	}
    };
    //Nếu ô hiện tại không có bom và số lượng bom trong danh sách các bom nhỏ 
    //hơn số lượng bom tối đa mà người 
    //chơi có thể giữ, một đối tượng kiểu Bomb mới sẽ được tạo ra
    //và được thêm vào danh sách các bom.

    //Sau khi danh sách các bom đã được cập nhật, phương thức notifyListeners() được gọi để thông báo cho các lớp khác biết rằng danh sách các bom đã được cập nhật.
   
    public Action dropBomb = new AbstractAction()
    {
	public void actionPerformed(ActionEvent e) {
	    if(!floor.squareHasBomb(getRowIndex(), getColIndex()) && floor.getBombListSize() < getBombCount()){
		floor.addToBombList(new Bomb(getRowIndex(), getColIndex(), getExplosionRadius()));
	    }
	    floor.notifyListeners();
	}
    };

    public Player(BombermanComponent bombermanComponent, Floor floor) {
	super(PLAYER_START_X, PLAYER_START_Y, PLAYER_PIXELS_BY_STEP);
	explosionRadius = 1;
	bombCount = 1;
	this.floor = floor;
	setPlayerButtons(bombermanComponent);
    }

    public void setPlayerButtons(BombermanComponent bombermanComponent){
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke("UP"), "moveUp");
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
	bombermanComponent.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "dropBomb");
	bombermanComponent.getActionMap().put("moveRight", right);
	bombermanComponent.getActionMap().put("moveLeft", left);
	bombermanComponent.getActionMap().put("moveUp", up);
	bombermanComponent.getActionMap().put("moveDown", down);
	bombermanComponent.getActionMap().put("dropBomb", dropBomb);
    }

    public int getBombCount() {
	return bombCount;
    }

    public void setBombCount(int bombCount) {
	this.bombCount = bombCount;
    }

    public int getExplosionRadius() {
	return explosionRadius;
    }

    public void setExplosionRadius(int explosionRadius) {
	this.explosionRadius = explosionRadius;
    }

    private void movePlayer(Move move) {
	move(move);
	if(floor.collisionWithBlock(this)){
	    moveBack(move);
	}
	if(floor.collisionWithBombs(this)){
	    moveBack(move);
	}
	if(floor.collisionWithEnemies()){
	    floor.setIsGameOver(true);
	}

	floor.checkIfPlayerLeftBomb();
	floor.collisionWithPowerup();
	floor.notifyListeners();
    }

}
