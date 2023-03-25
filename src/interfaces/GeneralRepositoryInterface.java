package interfaces;

public interface GeneralRepositoryInterface {
	void setMasterThiefState(int state);
	void setCommonThiefState(int thief_id, int state);
	void setCommonThiefSituation(int thief_id, char situation);
	void setCommonThiefMD(int thief_id, int md);
	void setAssPartElem(int thief_id, int room_id, int elem);
	void resetAssPartElem(int elem);
	void setCarryCanvas(int elem, int canvas); 
	void setCommonThiefPosition(int elem, int pos);
	void setRoomPaintings(int[] paintings);
	void setRoomDistance(int[] distances);
	void incStolenPaintings();
	void logInitialStatus();
	void logStatus();
	void logFinalStatus();	
}
