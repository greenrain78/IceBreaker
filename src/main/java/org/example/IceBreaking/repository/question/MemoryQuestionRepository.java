package org.example.IceBreaking.repository.question;

import org.example.IceBreaking.domain.Question;
import org.example.IceBreaking.dto.UserInterestDto;
import org.example.IceBreaking.enumData.InterestKeyword;
import org.example.IceBreaking.enumData.QuestionsByInterestKey;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryQuestionRepository implements QuestionRepository {

    private static final String welcomeQuestion = "전화번호를 입력해주세요.";
    private static final Map<InterestKeyword, QuestionsByInterestKey> questionMap = new HashMap<>();
    private final Map<Integer, Map<String, String[]>> teamInterestsMap = new HashMap<>();

    static {
        questionMap.put(InterestKeyword.SPORTS, QuestionsByInterestKey.SPORTS);
        questionMap.put(InterestKeyword.MOVIESANDDRAMA, QuestionsByInterestKey.MOVIESANDDRAMA);
        questionMap.put(InterestKeyword.MUSIC, QuestionsByInterestKey.MUSIC);
        questionMap.put(InterestKeyword.GAMES, QuestionsByInterestKey.GAMES);
        questionMap.put(InterestKeyword.TRIP, QuestionsByInterestKey.TRIP);
        questionMap.put(InterestKeyword.EATING, QuestionsByInterestKey.EATING);
        questionMap.put(InterestKeyword.LOVE, QuestionsByInterestKey.LOVE);
        questionMap.put(InterestKeyword.PET, QuestionsByInterestKey.PET);
        questionMap.put(InterestKeyword.BALANCEGAMES, QuestionsByInterestKey.BALANCEGAMES);
        questionMap.put(InterestKeyword.IF, QuestionsByInterestKey.IF);
    }

    @Override
    public Question findWelcomeQuestion() {
        return new Question(welcomeQuestion);
    }

    @Override
    public void saveInterestsByTeam(UserInterestDto userInterestDto) {
        // 채팅방에 입장한 user들의 관심사 정보를 저장 & 겹치는 것이 있는지 체크
        // 같은 userId값을 가진 User가 여러번 입장해도 한번만 put -> 이 기능 추가해야함

        String userId = userInterestDto.getUserId();
        int teamId = userInterestDto.getTeamId();
        String[] interests = userInterestDto.getInterests();

        Map<String, String[]> userInterestsMapInTeam = teamInterestsMap.get(teamId);
        if (userInterestsMapInTeam == null) {
            userInterestsMapInTeam = new HashMap<>();
        }

        // userInterestsMapInTeam 에 userId 별로 관심사 키워드 정보 추가
        /**
         * user가 관심사 정보를 수정하고 다시 채팅방 입장시에는 이 정보가 바껴야함 -> 추가 리펙토링 필요
         * dto에 user가 관심사를 수정했는지 아닌지를 check하는 flag 정보가 추가되어야함
         */
        if (!userInterestsMapInTeam.containsKey(userId)) {
            userInterestsMapInTeam.put(userId, interests);
        }

        teamInterestsMap.put(teamId, userInterestsMapInTeam);

        // 검증용
        System.out.println("teamId = " + teamId);
        for (String key : userInterestsMapInTeam.keySet()) {
            System.out.println(key + " : " + Arrays.toString(userInterestsMapInTeam.get(key)));
        }
    }

    @Override
    public Question findQuestionByTeamInterests(int teamId) {
        // teamId별로 user들의 관심사 list 정보를 count & 가중치를 다르게 부여해서 질문 return
        Map<String, String[]> userInterestsMapInTeam = teamInterestsMap.get(teamId);

        // 관심사 빈도 수를 저장할 맵 -> user가 채팅방을 나갔다 들어올 경우, 관심사를 변경했을수도 있으므로 매번 새로 계산
        // 추후에 리펙토링 필요할 듯
        Map<String, Integer> interestCountMap = new HashMap<>();

        for (String[] interests : userInterestsMapInTeam.values()) {
            for (String interest : interests) {
                interestCountMap.put(interest, interestCountMap.getOrDefault(interest, 0) + 1);
            }
        }

        // 가장 빈도수가 높은 관심사를 select
        int maxCount = Collections.max(interestCountMap.values());
        List<String> topInterests = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : interestCountMap.entrySet()) {
            if (entry.getValue() == maxCount) {
                topInterests.add(entry.getKey());
            }
        }

        // topInterests에서 랜덤으로 하나 선택
        Random random = new Random();
        String topInterest = topInterests.get(random.nextInt(topInterests.size()));

        // questionMap에서 topInterest에 해당하는 키워드의 질문들 get
        QuestionsByInterestKey questionsByInterestKey = questionMap.get(InterestKeyword.valueOf(topInterest.toUpperCase()));
        String[] questions = questionsByInterestKey.getQuestions();
        String selectQuestion = questions[random.nextInt(questions.length)];

        return new Question(selectQuestion);
    }
}
