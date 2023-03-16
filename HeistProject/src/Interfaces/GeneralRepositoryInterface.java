package Interfaces;

public interface GeneralRepositoryInterface {
	void CommonThiefLog(int type, int thief_id, int value);
	void MasterThiefLog(int value);
	void MuseumLog(int type, int room_id, int value);
	void AssaultPartyLog(int type, int assaultParty_id, int elem_id, int value);
	void FinalLog(int total_paintings);
}
