/*
 ************************************************************************************
 * Copyright (C) 2001-2009 encuestame: system online surveys Copyright (C) 2009
 * encuestame Development Team.
 * Licensed under the Apache Software License version 2.0
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to  in writing,  software  distributed
 * under the License is distributed  on  an  "AS IS"  BASIS,  WITHOUT  WARRANTIES  OR
 * CONDITIONS OF ANY KIND, either  express  or  implied.  See  the  License  for  the
 * specific language governing permissions and limitations under the License.
 ************************************************************************************
 */
package org.encuestame.core.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.encuestame.core.exception.EnMeExpcetion;
import org.encuestame.core.mail.MailServiceImpl;
import org.encuestame.core.persistence.pojo.QuestionPattern;
import org.encuestame.core.persistence.pojo.Questions;
import org.encuestame.core.persistence.pojo.QuestionsAnswers;
import org.encuestame.core.persistence.pojo.SecUsers;
import org.encuestame.core.persistence.pojo.TweetPoll;
import org.encuestame.core.service.AbstractSurveyService;
import org.encuestame.core.service.ISurveyService;
import org.encuestame.core.test.service.config.AbstractBaseUnitBeans;
import org.encuestame.utils.web.UnitAnswersBean;
import org.encuestame.utils.web.UnitPatternBean;
import org.encuestame.utils.web.UnitQuestionBean;
import org.encuestame.utils.web.UnitTweetPoll;
import org.encuestame.utils.web.UnitTweetPollResult;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;

import twitter4j.Status;

/**
 * Test of {@link AbstractSurveyService}
 * @author Picado, Juan juan@encuestame.org
 * @since 05/12/2009 15:04:56
 * @version $Id$
 */

public class TestSurveyService  extends AbstractBaseUnitBeans{

    /** {@link AbstractSurveyService} */
    @Autowired
    private ISurveyService surveyService;

    @Autowired
    private MailServiceImpl mailServiceImpl;

    /** {@link Questions} */
    private Questions question;

    /** {@link QuestionPattern} **/
    private QuestionPattern pattern;

    /** {@link SecUsers} **/
    private SecUsers user;

    private List<UnitAnswersBean> answers;

    private UnitAnswersBean answersBean;

    /** {@link UnitQuestionBean} **/
    private UnitQuestionBean questionBean;

    /** {@link UnitPatternBean}**/
    private UnitPatternBean patternBean;
     /**
     *
     */
    @Before
    public void serviceInit(){

         surveyService.setServiceMail(mailServiceImpl);
         this.user = createUser("testEncuesta", "testEncuesta123");
         this.question = createQuestion("Why the sky is blue?","html");
         this.pattern = createQuestionPattern("html");
         createQuestionAnswer("Yes", this.question,"SSSA");
         //this.questionBean = createUnitQuestionBean("", 1L, 1L, listAnswers, pattern)
         answers = new ArrayList<UnitAnswersBean>();
         answers.add(createAnswersBean("2DFAAS", "Yes", question.getQid()));
         answers.add(createAnswersBean("4DSWGK", "No", question.getQid()));

         patternBean = createPatternBean("radio.class",
                    "radio buttons", "2", "Yes/No", "template.php");

            questionBean = createUnitQuestionBean("questionName", 1L, this.user.getUid(),
                    this.answers, patternBean);
    }

    /**
     * Test Load All Questions.
     * @throws EnMeExpcetion exception
     */
    @Test
    public void testloadAllQuestions() throws EnMeExpcetion{
        final List<UnitQuestionBean> alist = surveyService.loadAllQuestions();
        assertEquals("Should be equals", 1, alist.size());
    }

    /**
     * Load Patter Info Null.
     * @throws EnMeExpcetion exception
     */
    @Test
    @ExpectedException(EnMeExpcetion.class)
    public void testloadPatternInfoNull() throws EnMeExpcetion {
        surveyService.loadPatternInfo(null);
    }

    /**
     * Load Patter Info.
     * @throws EnMeExpcetion exception
     */
    @Test
    public void testloadPatternInfo() throws EnMeExpcetion {
    //  this.serviceInit();
        UnitPatternBean patternBean = new UnitPatternBean(this.pattern.getPatternId(),"descPattern","label",
                "patronType", "template","classpattern","levelpattern","finallity");
    //    patternBean.setId(createQuestionPattern("html").getPatternId());
        patternBean = surveyService.loadPatternInfo(patternBean);
    // assertNotNull(patternBean);
    assertEquals("Should be equals",patternBean.getPatronType(), getPattern().getPatternType());
    }

    /**
     * Load All Patterns.
     * @throws EnMeExpcetion exception
     */
    @Test
    public void testloadAllPatrons() throws EnMeExpcetion {
    // this.serviceInit();
        final Collection<UnitPatternBean> patternList = surveyService.loadAllPatrons();
    // assertNotNull(patternList);
        assertEquals("Should be equals",2, patternList.size());
    }

    /**
     * Load All Patterns Zero Results.
     * @throws EnMeExpcetion exception
     */
//  @Test
    public void testloadAllPatronsZeroResults() throws EnMeExpcetion {
        final Collection<UnitPatternBean> patternList = surveyService.loadAllPatrons();
        assertNotNull(patternList);
        assertEquals("Should be equals",0, patternList.size());
    }

    /**
     * Test Create Tweet Poll.
     * @throws EnMeExpcetion exception
     */
    @Test
    public void testCreateTweetPoll() throws EnMeExpcetion{
    final Questions question = createQuestion("why the sky is blue?", "yes/no", this.user);

    createQuestionAnswer("yes", question, "12345");
    createQuestionAnswer("no", question, "12346");
    final UnitTweetPoll tweetPollBean = new UnitTweetPoll();
    final UnitQuestionBean questionBean = new UnitQuestionBean();
    questionBean.setId(question.getQid());
    tweetPollBean.setQuestionBean(questionBean);
    tweetPollBean.setPublishPoll(true);
    tweetPollBean.setScheduleDate(new Date());
    tweetPollBean.setCompleted(false);
    tweetPollBean.setUserId(this.user.getUid());
    this.surveyService.createTweetPoll(tweetPollBean);
    final String s = this.surveyService.generateTweetPollText(tweetPollBean,  RandomStringUtils.randomAlphabetic(15));
    final Status status = this.surveyService.publicTweetPoll(s, this.user.getTwitterAccount(), this.user.getTwitterPassword());
    assertNotNull(status.getId());
    }

    /**
     * Service to retrieve Results TweetPoll  Id.
     */
    @Test
    public void testGetResultsByTweetPollId(){
    final TweetPoll tweetPoll = createFastTweetPollVotes();
    final List<UnitTweetPollResult> results = this.surveyService.getResultsByTweetPollId(tweetPoll.getTweetPollId());
    assertEquals("Should be equals", 2 , results.size());
    }


    /**
     * Test Create Question.
     * @throws EnMeExpcetion
     **/
    @Test
    public void testCreateQuestion() throws EnMeExpcetion {

        surveyService.createQuestion(this.questionBean);
        assertNotNull(questionBean);
    }

    /**
    * Test Save Answers.
     * @throws EnMeExpcetion
    **/
    @Test
    public void testSaveAnswers() throws EnMeExpcetion{
        final UnitAnswersBean answersBean = createAnswersBean("ASJKE", "Yes", this.question.getQid());
        surveyService.saveAnswer(answersBean);
        assertNotNull(answersBean.getAnswerId());
    }

    /**
     * Test Retrieve Answer By Question Id.
     **/
    @Test
    public void testRetrieveAnswerByQuestionId(){

           final List<UnitAnswersBean> listUnitAnswerBean = surveyService.retrieveAnswerByQuestionId(this.question.getQid());
           System.out.println(this.question.getQid());
           System.out.println("Question ID");
           assertEquals("Should be equals",1, listUnitAnswerBean.size());

    }

    /**
     * Test Get Tweets
     */

    @Test
    public void testGetTweetsPollByUserId(){
         final TweetPoll tweetPollnew = createTweetPoll(2L, false, false, false, false, false, new Date(), new Date(), false,
                                                   this.user, this.question);
         createQuestionAnswer("Yes", this.question, "BBB");
         createQuestionAnswer("No", this.question, "CCC");
         final List<UnitTweetPoll> unitTweetPollList = surveyService.getTweetsPollsByUserId(this.user.getUid());
         assertEquals("Should be equals", 1, unitTweetPollList.size());
    }

    /**
     * Test Save Tweet Id.
     * @throws EnMeExpcetion
     */

    public void testSaveTweetId() throws EnMeExpcetion{
        Questions questionSave = createQuestion("Why the sea is blue?","html");
        final String tweetUrl = "http://www.encuestame.org";
        final TweetPoll tweetPoll = createTweetPollPublicated(true, true, new Date(), this.user, questionSave);

        final UnitTweetPoll unitTweetPoll = createUnitTweetPollPublicated(new Date(), true, tweetUrl, this.user.getUid(),
                                            this.questionBean, this.user.getTwitterAccount());

        unitTweetPoll.setId(tweetPoll.getTweetPollId());
        final String s = this.surveyService.generateTweetPollText(unitTweetPoll,  RandomStringUtils.randomAlphabetic(15));
        System.out.println(s);
        System.out.println(this.user.getTwitterAccount());
        System.out.println(this.user.getTwitterPassword());
        final Status status = this.surveyService.publicTweetPoll(s, this.user.getTwitterAccount(), this.user.getTwitterPassword());
        assertNotNull(status.getId());
        unitTweetPoll.setTweetId(status.getId());
         surveyService.saveTweetId(unitTweetPoll);
        assertNotNull(unitTweetPoll.getTweetId());


    }

    /**
     * Test Tweet Poll Vote
     */
    public void testTweetPollVote(){

    }

    /**
     * Test Generate Tweet Poll Text.
     * @throws EnMeExpcetion EnMeExpcetion
     */
    @Test
    public void testGenerateTweetPollText() throws EnMeExpcetion{
        final TweetPoll tweetPollPublicate = createTweetPollPublicated(true,true,new Date(), this.user, this.question);
        createQuestionAnswer("Yes", this.question, "EEEE");
        createQuestionAnswer("No", this.question, "FFFF");
        final String tweetUrl = "http://www.encuestame.org";
        final UnitTweetPoll uniTweetPoll = createUnitTweetPollPublicated(new Date(), true, tweetUrl, this.user.getUid(), this.questionBean, "testtweetuser");
        uniTweetPoll.setId(tweetPollPublicate.getTweetPollId());
        final String twettQuestionText = surveyService.generateTweetPollText(uniTweetPoll, tweetUrl);
        assertNotNull(twettQuestionText);
        final Integer textLength = twettQuestionText.length();
        assertEquals(true, (textLength < 140 ? true: false));
        }


    /**
     * Test Update Answer By Answer Id.
     * @throws EnMeExpcetion
     */
    @Test
    public void testUpdateAnswersByAnswerId() throws EnMeExpcetion{
        final String expectedResponse = "Quizas";
        final QuestionsAnswers questionAnswers = createQuestionAnswer("No", this.question, "HASH");
        surveyService.updateAnswerByAnswerId(questionAnswers.getQuestionAnswerId(), expectedResponse);
        assertEquals(questionAnswers.getAnswer(), expectedResponse);
    }

    /**
     * @param surveyService the surveyService to set
     */
    public void setSurveyService(final ISurveyService surveyService) {
        this.surveyService = surveyService;
    }
    /**
     * @return the question
     */
    public Questions getQuestion() {
        return question;
    }
    /**
     * @return the pattern
     */
    public QuestionPattern getPattern() {
        return pattern;
    }

    /**
     * @param mailServiceImpl the mailServiceImpl to set
     */
    public void setMailServiceImpl(final MailServiceImpl mailServiceImpl) {
        this.mailServiceImpl = mailServiceImpl;
    }

}