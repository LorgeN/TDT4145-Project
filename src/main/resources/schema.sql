CREATE TABLE User(
                     Email VARCHAR(128) PRIMARY KEY NOT NULL,
                     Name VARCHAR(128) NOT NULL,
                     Password VARCHAR(32) NOT NULL
);

CREATE TABLE ActiveDays(
                           User VARCHAR(128) NOT NULL,
                           Date DATE NOT NULL,
                           PRIMARY KEY(User, Date),
                           CONSTRAINT fk_activedays_user FOREIGN KEY (User)
                               REFERENCES User(Email)
                               ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Course(
                       CourseId INT AUTO_INCREMENT PRIMARY KEY,
                       Name VARCHAR(64) NOT NULL,
                       Term VARCHAR(32) NOT NULL,
                       AllowAnonymous BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Participant(
                            User VARCHAR(128) NOT NULL,
                            CourseId INT NOT NULL,
                            IsInstructor BOOLEAN NOT NULL DEFAULT FALSE,
                            PRIMARY KEY (User, CourseId),
                            CONSTRAINT fk_participant_user FOREIGN KEY (User)
                                REFERENCES User(Email)
                                ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT fk_participant_course FOREIGN KEY (CourseId)
                                REFERENCES Course(CourseId)
                                ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Tag(
                    CourseId INT NOT NULL,
                    Name VARCHAR(64) NOT NULL,
                    PRIMARY KEY (CourseId, Name),
                    CONSTRAINT fk_tag_course FOREIGN KEY (CourseId)
                        REFERENCES Course(CourseId)
                        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Folder(
                       FolderId INT AUTO_INCREMENT PRIMARY KEY,
                       Name VARCHAR(64) NOT NULL,
                       ParentFolderId INT DEFAULT NULL,
                       CourseId INT NOT NULL,
    /* Sikrer at ingen mapper paa samme nivaa heter det samme.*/
                       CONSTRAINT i_folder_name_enforce UNIQUE INDEX (Name, ParentFolderId, CourseId),
                       CONSTRAINT fk_folder_course FOREIGN KEY (Courseid)
                           REFERENCES Course(CourseId)
                           ON DELETE CASCADE ON UPDATE CASCADE,
                       CONSTRAINT fk_folder_parent FOREIGN KEY (ParentFolderId)
                           REFERENCES Folder(FolderId)
                           ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Thread(
                       ThreadId INT AUTO_INCREMENT PRIMARY KEY,
                       Title TEXT NOT NULL,
                       CourseId INT NOT NULL,
                       FolderId INT NOT NULL,
                       Tag VARCHAR(64) NOT NULL,
                       CONSTRAINT fk_thread_course FOREIGN KEY (CourseId)
                           REFERENCES Course(CourseId)
                           ON DELETE CASCADE ON UPDATE CASCADE,
                       CONSTRAINT fk_thread_folder FOREIGN KEY (FolderId)
                           REFERENCES Folder(FolderId)
                           ON DELETE CASCADE ON UPDATE CASCADE,
                       CONSTRAINT fk_thread_tag FOREIGN KEY (CourseId, Tag)
                           REFERENCES Tag(CourseId, Name)
                           ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Post(
                     PostId INT AUTO_INCREMENT PRIMARY KEY,
                     ThreadId INT NOT NULL,
                     IsRoot BOOLEAN NOT NULL DEFAULT FALSE,
                     Anonymous BOOLEAN NOT NULL DEFAULT FALSE,
                     PostedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     Text LONGTEXT NOT NULL,
                     CreatedByUser VARCHAR(128) NOT NULL,
                     CONSTRAINT fk_post_thread FOREIGN KEY (ThreadId)
                         REFERENCES Thread(ThreadId)
                         ON DELETE CASCADE ON UPDATE CASCADE,
                     CONSTRAINT fk_post_created_by FOREIGN KEY (CreatedByUser)
                         REFERENCES User(Email)
                         ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE GoodComment(
                            User VARCHAR(128) NOT NULL,
                            PostId INT NOT NULL,
                            PRIMARY KEY (User, PostId),
                            CONSTRAINT fk_goodcomment_user FOREIGN KEY (User)
                                REFERENCES User(Email)
                                ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT fk_goodcomment_post FOREIGN KEY (PostId)
                                REFERENCES Post(PostId)
                                ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE PostRead(
                         User VARCHAR(128) NOT NULL,
                         PostId INT NOT NULL,
                         PRIMARY KEY (User, PostId),
                         CONSTRAINT fk_postread_user FOREIGN KEY (User)
                             REFERENCES User(Email)
                             ON DELETE CASCADE ON UPDATE CASCADE,
                         CONSTRAINT fk_postread_post FOREIGN KEY (PostId)
                             REFERENCES Post(PostId)
                             ON DELETE CASCADE ON UPDATE CASCADE
);
