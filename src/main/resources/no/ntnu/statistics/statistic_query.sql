SELECT User.Name,
       User.Email,
       Count(DISTINCT PR.PostId) as antall_lest,
       Count(DISTINCT P2.PostId) as antall_opprettet
FROM User
         LEFT OUTER JOIN (
    PostRead PR
        LEFT OUTER JOIN Post P1 ON (PR.PostId = P1.PostId)
        LEFT OUTER JOIN Thread T1 ON (P1.ThreadId = T1.ThreadId)
    ) ON (User.Email = PR.User)
         LEFT OUTER JOIN Post P2 ON (Email = P2.CreatedByUser)
         LEFT OUTER JOIN Thread T2 ON (P2.ThreadId = T2.ThreadId)
WHERE T1.CourseId = ?
   OR T2.CourseId = ?
   OR T1.CourseId IS NULL
   OR T2.CourseId IS NULL
GROUP BY User.Email
ORDER BY antall_lest DESC;