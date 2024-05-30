package org.example.IceBreaking.repository.question;

import org.example.IceBreaking.domain.Question;
import org.example.IceBreaking.dto.UserInterestDto;

public interface QuestionRepository {

    Question findWelcomeQuestion();

    void saveInterestsByTeam(UserInterestDto userInterestDto);

    Question findQuestionByTeamInterests(int teamId);
}
