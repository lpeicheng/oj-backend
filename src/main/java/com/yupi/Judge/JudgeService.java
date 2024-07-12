package com.yupi.Judge;

import com.yupi.model.entity.QuestionSubmit;

public interface JudgeService {
   QuestionSubmit doJudge(Long questionId);
}
