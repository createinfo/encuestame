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
package org.encuestame.persistence.dao.imp;

import java.util.Collection;
import java.util.List;

import org.encuestame.persistence.dao.ISurvey;
import org.encuestame.persistence.domain.survey.SurveyFolder;
import org.encuestame.persistence.domain.survey.SurveyFormat;
import org.encuestame.persistence.domain.survey.SurveyPagination;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Survey Dao.
 * @author Picado, Juan juan@encuestame.org
 * @since June 01, 2009
 * @version $Id$
 */
@Repository
public class SurveyDaoImp extends AbstractHibernateDaoSupport implements ISurvey {

    /**
     * Search survey by name.
     * @param searchString search string
     * @return {@link Collection} of {@link SurveyFormat}
     * @throws HibernateException
     */
    @SuppressWarnings("unchecked")
    public Collection<SurveyFormat> searchSurveyByName(String searchString)
            throws HibernateException {
        return getSession().createCriteria(SurveyFormat.class)
        .add(Restrictions.like("name","%"+searchString+"%"))
        //TODO: Add Max Results parameter
        .setMaxResults(10).list();

    }

    /**
     * Retrieve Survey Folders By UserId
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SurveyFolder> retrieveFolderByUserId(final Long userId){
        return getHibernateTemplate().findByNamedParam("FROM SurveyFolder where users.uid=:userId","userId", userId);
    }

    /**
     * Retrieve Surveys by Folder
     * @param userId
     * @param folderId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SurveyFolder> retrieveSurveysByFolder(final Long userId, final Long folderId){
        return getHibernateTemplate().findByNamedParam("FROM SurveyFolder where surveyFolderId:folderId","folderId", folderId);
    }

    /**
     * Retrieve All Folders
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SurveyFolder> retrieveAllFolders(){
        return getHibernateTemplate().find("FROM SurveyFolder");
    }

    /**
     * Retrieve Sections By Page.
     * @param surveyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SurveyPagination> retrieveSectionsByPage(final Long surveyId){
        return getHibernateTemplate().findByNamedParam("FROM SurveyFolder where surveyFolderId:folderId","folderId", surveyId);
    }

}