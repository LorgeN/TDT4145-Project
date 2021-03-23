SELECT User.Email,
       User.Name,
       COUNT(DISTINCT Pr.PostId) AS antall_lest,
       COUNT(DISTINCT P2.PostId) AS antall_opprettet
FROM (User INNER JOIN Participant P on User.Email = P.User)
         INNER JOIN Course C ON C.CourseId = P.CourseId
         LEFT OUTER JOIN (
    PostRead PR INNER JOIN Post P1 ON (PR.PostId = P1.PostId)
        INNER JOIN Thread T on P1.ThreadId = T.ThreadId
    ) ON (T.CourseId = C.CourseId AND User.Email = PR.User)
         LEFT OUTER JOIN (
    Post P2 INNER JOIN Thread T2 on P2.ThreadId = T2.ThreadId
    ) ON (User.Email = P2.CreatedByUser AND T2.CourseId = C.CourseId)

WHERE C.CourseId = ?
GROUP BY User.Email
ORDER BY antall_lest DESC;
