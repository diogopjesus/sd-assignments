package main;

import entities.*;
import genclass.*;
import sharedRegions.*;

/**
 * Simulation of the heist to the museum.
 */
public class HeistToTheMuseum {
    /**
     * Main method.
     *
     * @param args runtime arguments
     */
    public static void main(String[] args) {
        String fileName;
        char opt;
        boolean success;

        MasterThief masterThief;
        OrdinaryThief[] ordinaryThieves = new OrdinaryThief[SimulPar.M - 1];

        AssaultParty[] assaultParties = new AssaultParty[(SimulPar.M - 1) / SimulPar.K];
        ConcentrationSite concentSite;
        ControlCollectionSite contColSite;
        GeneralRepository repos;
        Museum museum;

        GenericIO.writelnString("\n" + "      Heist to the Museum\n");
        do {
            GenericIO.writeString("Logging file name? ");
            fileName = GenericIO.readlnString();
            if (FileOp.exists(".", fileName)) {
                do {
                    GenericIO.writeString(
                            "There is already a file with this name. Delete it (y - yes; n - no)? ");
                    opt = GenericIO.readlnChar();
                } while ((opt != 'y') && (opt != 'n'));
                if (opt == 'y')
                    success = true;
                else
                    success = false;
            } else
                success = true;
        } while (!success);

        int[] maxDis = new int[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++)
            maxDis[i] = SimulPar.md + (int) Math.round(Math.random() * (SimulPar.MD - SimulPar.md));

        int[] numPaint = new int[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++)
            numPaint[i] = SimulPar.p + (int) Math.round(Math.random() * (SimulPar.P - SimulPar.p));

        int[] roomDist = new int[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++)
            roomDist[i] = SimulPar.d + (int) Math.round(Math.random() * (SimulPar.D - SimulPar.d));

        // Initialize simulation
        repos = new GeneralRepository(fileName, maxDis, numPaint, roomDist);

        for (int i = 0; i < (SimulPar.M - 1) / SimulPar.K; i++)
            assaultParties[i] = new AssaultParty(repos, i);

        museum = new Museum(repos, assaultParties, numPaint, roomDist);

        contColSite = new ControlCollectionSite(repos, assaultParties);

        concentSite = new ConcentrationSite(repos, contColSite, assaultParties, museum);

        masterThief = new MasterThief(contColSite, concentSite, assaultParties);

        for (int i = 0; i < SimulPar.M - 1; i++)
            ordinaryThieves[i] = new OrdinaryThief(contColSite, concentSite, assaultParties, museum,
                    i, maxDis[i]);

        masterThief.start();
        for (int i = 0; i < SimulPar.M - 1; i++)
            ordinaryThieves[i].start();

        /* Wait for the end of the simulation */
        GenericIO.writelnString();
        for (int i = 0; i < SimulPar.M - 1; i++) {
            try {
                ordinaryThieves[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("The ordinary thief " + (i + 1) + " has terminated.");
        }
        GenericIO.writelnString();
        try {
            masterThief.join();
        } catch (InterruptedException e) {
        }
        GenericIO.writelnString("The master thief has terminated.");
        GenericIO.writelnString();
    }
}
