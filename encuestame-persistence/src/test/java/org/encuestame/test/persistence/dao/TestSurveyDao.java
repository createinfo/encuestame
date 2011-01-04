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
package org.encuestame.test.persistence.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.encuestame.persistence.domain.security.Account;
import org.encuestame.persistence.domain.security.UserAccount;
import org.encuestame.persistence.domain.survey.Survey;
import org.encuestame.persistence.domain.survey.SurveyFolder;
import org.encuestame.persistence.domain.survey.SurveyPagination;
import org.encuestame.persistence.domain.survey.SurveySection;
import org.encuestame.persistence.exception.EnMeDomainNotFoundException;
import org.encuestame.test.config.AbstractBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link Survey}..
 * @author Morales, Urbina Diana paola AT encuestame.org
 * @since October 28, 2010
 * @version $Id: $
 */
public class TestSurveyDao extends AbstractBase {

    /** {@link Account}.**/
    Account user;

    /** {@link UserAccount}. **/
    private UserAccount secondaryUser;

    /** {@link SurveyFolder}. **/
    private SurveyFolder surveyFolder;

    /** {@link SurveySection}. **/
    private SurveySection surveySection;

    /** {@link SurveyPagination}. **/
    private SurveyPagination surveyPag;

    private Survey survey;

    @Before
    public void initData(){
        this.user  = createUser();
        this.secondaryUser = createSecondaryUser("paola", this.user);
        this.survey = createDefaultSurvey(user);
        this.surveyFolder = createSurveyFolders("My Survey Folder", user);
        this.surveySection = createSurveySection("My Section");
     }

    /**
     * Test Search Survey by Username.
     */
    @Test
    public void testSearchSurveyByName(){
        final List<Survey> surveyResult = getSurveyDaoImp().searchSurveyByUserId("First", user.getUid());
        assertEquals("Should be equals", 1, surveyResult.size());
       }

    /**
     * Test Retrieve Folders by User Id.
     */
    @Test
    public void testRetrieveFolderByUserId(){
        final List<SurveyFolder> folders = getSurveyDaoImp().retrieveFolderByUserId(user.getUid());
        assertEquals("Should be equals", 1, folders.size());
    }

    /**
     * Test Retrieve Survey Section by Id.
     */
    @Test
    public void testRetrieveSurveySectionById(){
        final SurveySection section = getSurveyDaoImp().retrieveSurveySectionById(surveySection.getSsid());
        assertEquals("Should be equals", "My Section", section.getDescSection());
    }


    /**
     * Test Retrieve Questions by Survey Section.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRetrieveQuestionsBySurveySection(){

        final List questionList = getSurveyDaoImp().retrieveQuestionsBySurveySection(surveySection.getSsid());
        assertEquals("Should be equals", 3, questionList.size());
     }

    @SuppressWarnings("unchecked")
   // @Test
    public void testRetrieveSectionByPagination(){
         this.surveyPag = createSurveyPagination(1, surveySection,this.survey);
         final SurveySection s2 = createSurveySection("Second Section");
         createSurveyPagination(1, s2, this.survey);
         //System.out.println(surveyPag.getPageNumber());
         //System.out.println(surveyPag.getSurveySection().getSsid());
         //System.out.println(surveyPag.getSurveySection().getDescSection());
         //System.out.println(surveyPag.getSurvey().getName());

         final List sectionsByPage = getSurveyDaoImp().retrieveSectionByPagination(surveyPag.getPageNumber());
         assertEquals("Should be equals", 2, sectionsByPage.size());
    }

    /**
     * Test Get Survey by User.
     */
    @Test
    public void testGetSurveyByIdandUserId(){
        assertNotNull(this.survey);
        assertNotNull(this.user);
        final Survey mySurvey = getSurveyDaoImp().getSurveyByIdandUserId(this.survey.getSid(), this.user.getUid());
        assertNotNull(mySurvey.getSid());
        assertEquals("Should be equals", this.survey.getSid(), mySurvey.getSid());
    }

    /**
     * Test Get Survey Folder
     */
    @Test
    public void testGetSurveyFolderByIdandUser(){
        assertNotNull(this.surveyFolder);
        assertNotNull(this.user);
        final SurveyFolder folder = getSurveyDaoImp().getSurveyFolderByIdandUser(this.surveyFolder.getId(), this.user.getUid());
        assertNotNull(folder.getId());
        assertEquals("Should be equals", this.surveyFolder.getId(), surveyFolder.getId());
    }

    /**
     * Test Get Survey by Id.
     */
    @Test
    public void testGetSurveyFolderById(){
        assertNotNull(this.surveyFolder);
        final SurveyFolder folder = getSurveyDaoImp().getSurveyFolderById(this.surveyFolder.getId());
        assertNotNull(folder.getId());
        System.out.println("SURVEY FOLDER ID--->"+ this.surveyFolder.getId());
        System.out.println("MY SURVEY FOLDER--->"+ surveyFolder.getId());
        assertEquals("Should be equals", this.surveyFolder.getId(), surveyFolder.getId());
    }

    /**
     * Test Retrieve Survey by Folder.
     * @throws EnMeDomainNotFoundException
     */
    @Test
    public void testRetrieveSurveysByFolder() throws EnMeDomainNotFoundException{
        assertNotNull(surveyFolder);
        assertNotNull(survey);
        final Survey addSurvey = addSurveyToFolder(this.surveyFolder.getId(), this.user.getUid(), this.survey.getSid());
        assertNotNull(addSurvey);
        final List<Survey> sfolder = getSurveyDaoImp().retrieveSurveyByFolder(this.user.getUid(), this.surveyFolder.getId());
        assertEquals("Should be equals", 1, sfolder.size());
    }

    @Test
    public void testRetrieveAllFolders(){
         assertNotNull(surveyFolder);
         final List<SurveyFolder> allSurveyFolder = getSurveyDaoImp().retrieveAllFolders(this.user.getUid());
         assertEquals("Should be equals", 1, allSurveyFolder.size());
    }


}
