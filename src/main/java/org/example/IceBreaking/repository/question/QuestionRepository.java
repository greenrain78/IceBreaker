package org.example.IceBreaking.repository.question;

import org.example.IceBreaking.domain.Question;
import org.example.IceBreaking.dto.UserInterestDto;

import java.util.Map;

public interface QuestionRepository {

    Question findWelcomeQuestion();

    void saveInterestsByTeam(UserInterestDto userInterestDto);

    Question findQuestionByTeamInterests(int teamId, int questionIndex);

    Map<String, Integer> findAllInterestsByTeam(int teamId);
}
