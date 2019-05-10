package com.everhardsoft.liarsdice.game.master;

import android.os.SystemClock;

import java.util.Collections;
import java.util.Random;
import java.util.Stack;

/**
 * Created by faisa on 11/11/2016.
 */

public class LDMaster {
    protected Stack<String> players;
    protected String playingPlayer;
    protected int currentDiceValue;
    protected int currentDiceQuantity;
    public final int FAIL = 0;
    public final int SUCCESS = 1;
    public final int WRONG_PLAYER = 2;
    public LDMaster() {
    }
    public void startRound(String... players) {
        this.players = new Stack<>();
        Collections.addAll(this.players, players);
        Collections.shuffle(this.players, new Random(SystemClock.uptimeMillis()));
        currentDiceValue = 0;
        currentDiceQuantity = 0;
        playingPlayer = this.players.peek();
    }
    public void removePlayer(String player) {
        if(player.equals(playingPlayer))
            endPlayingPlayer();
        players.remove(player);
    }
    public void endPlayingPlayer() {
        int maximumIndex = players.size() - 1;
        int playerIndex = players.indexOf(playingPlayer);
        if(playerIndex >= maximumIndex)
            playingPlayer = players.peek();
        else
            playingPlayer = players.elementAt(playerIndex + 1);
    }
    public int bid(String player, int diceValue, int diceQuantity) {
        if(!player.equals(playingPlayer))
            return WRONG_PLAYER;
        if(diceValue > 6 || diceValue < 1 || diceQuantity <= 0 || !player.equals(playingPlayer))
            return FAIL;
        if(
                (diceValue > currentDiceValue) ||
                (diceQuantity > currentDiceQuantity)) {
            currentDiceValue = diceValue;
            currentDiceQuantity = diceQuantity;
        }
        return SUCCESS;
    }
    public int call(String player) {
        if(!player.equals(playingPlayer))
            return WRONG_PLAYER;
        return SUCCESS;
    }
    public int spot(String player) {
        if(!player.equals(playingPlayer))
            return WRONG_PLAYER;
        return SUCCESS;
    }
    public int getCurrentDiceValue() {
        return currentDiceValue;
    }
    public int getCurrentDiceQuantity() {
        return currentDiceQuantity;
    }
}
