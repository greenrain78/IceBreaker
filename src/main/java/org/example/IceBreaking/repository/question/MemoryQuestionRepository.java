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

    private final Map<Integer, Map<String, String[]>> teamInterestsMap = new HashMap<>();       // key : teamId, value : key가 userId이고 value가 user의 interests 인 map
    private final Map<Integer, Map<String, Integer>> teamInterestsCountMap = new HashMap<>();   // key : teamId, value : key가 관심사키워드 이고 value가 count값인 map

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

        String userId = userInterestDto.getUserId();
        int teamId = userInterestDto.getTeamId();
        String[] interests = userInterestDto.getInterests();

        Map<String, String[]> userInterestsMapInTeam = teamInterestsMap.get(teamId);
        if (userInterestsMapInTeam == null) {
            userInterestsMapInTeam = new HashMap<>();
        }

        // userInterestsMapInTeam 에 userId 별로 관심사 키워드 정보 추가
        userInterestsMapInTeam.put(userId, interests);

        teamInterestsMap.put(teamId, userInterestsMapInTeam);
        // 검증용
        System.out.println("teamId = " + teamId);
        for (String key : userInterestsMapInTeam.keySet()) {
            System.out.println(key + " : " + Arrays.toString(userInterestsMapInTeam.get(key)));
        }

        // team별로 관심사 키워드에 해당하는 count값 update
        Map<String, Integer> interestCountMap = new HashMap<>();
        for (String[] savedInterests : userInterestsMapInTeam.values()) {
            for (String interest : savedInterests) {
                interestCountMap.put(interest, interestCountMap.getOrDefault(interest, 0) + 1);
            }
        }
        teamInterestsCountMap.put(teamId, interestCountMap);
        // 검증용
        for (String key : interestCountMap.keySet()) {
            System.out.println(key + ":" + interestCountMap.get(key));
        }
    }

    @Override
    public Question findQuestionByTeamInterests(int teamId, int questionIndex) {
        Map<String, Integer> interestCountMap = teamInterestsCountMap.get(teamId);

        // 전체 관심사 키워드의 빈도수 합계 계산
        int totalInterests = interestCountMap.values().stream().mapToInt(Integer::intValue).sum();

        // questionIndex가 전체 빈도수 합계보다 큰 경우, 모듈로 연산을 통해 순환하도록 조정
        int targetIndex = questionIndex % totalInterests;

        // targetIndex에 해당하는 관심사 키워드 찾기
        int currentIndex = 0;
        String selectedInterest = null;
        for (Map.Entry<String, Integer> entry : interestCountMap.entrySet()) {
            currentIndex += entry.getValue();
            if (currentIndex > targetIndex) {
                selectedInterest = entry.getKey();
                break;
            }
        }
        System.out.println("targetIndex = " + targetIndex);
        System.out.println("selectedInterest = " + selectedInterest);

        // 해당 관심사 키워드에 해당하는 질문들 중 하나를 랜덤으로 get & return
        Random random = new Random();
        QuestionsByInterestKey questionsByInterestKey = questionMap.get(InterestKeyword.valueOf(selectedInterest.toUpperCase()));
        String[] questions = questionsByInterestKey.getQuestions();
        String selectQuestion = questions[random.nextInt(questions.length)];

        return new Question(selectQuestion);
    }
    @Override
    public Map<String, Integer> findAllInterestsByTeam(int teamId) {
        return teamInterestsCountMap.get(teamId);
    }
}
