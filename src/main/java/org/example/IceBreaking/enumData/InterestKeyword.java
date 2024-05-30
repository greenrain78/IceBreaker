package org.example.IceBreaking.enumData;

import lombok.Getter;

@Getter
public enum InterestKeyword {

    SPORTS("sports"),
    MOVIESANDDRAMA("movies/drama"),
    MUSIC("music"),
    GAMES("games"),
    TRIP("trip"),
    EATING("eating"),
    LOVE("love"),
    PET("pet"),
    BALANCEGAMES("balanceGames"),
    IF("if");

    private final String interestValue;

    InterestKeyword(String interestValue) {
        this.interestValue = interestValue;
    }
}
