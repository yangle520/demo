SELECT userId,userName,workDate,
IF(ISNULL(actIn1),0,(actIn1 > in1)) +IF(ISNULL(actOut1),0,(actOut1 < out1))  + IF(ISNULL(actIn2),0,(actIn2 > in2)) + IF(ISNULL(actOut2),0,(actOut2 < out2)) as lateTimes,
ISNULL(actIn1) + ISNULL(actOut1) + ISNULL(actIn2) + ISNULL(actOut2) as  forgetTimes
from (
		select u.userId,u.userName,o.workDate,
		w.arrangeIn1 as in1,
		MIN(case WHEN ("0000" <= MOD(t.checkTime,10000)) and (MOD(t.checkTime,10000) < ((w.arrangeIn1 + w.arrangeOut1)/2))  THEN  MOD(t.checkTime,10000) ELSE NULL END) as actIn1,
		w.arrangeOut1 as out1,
		MAX(case WHEN (((w.arrangeIn1 + w.arrangeOut1)/2) <= MOD(t.checkTime,10000)) and (MOD(t.checkTime,10000) < ((w.arrangeOut1 + w.arrangeIn2)/2))  THEN  MOD(t.checkTime,10000) ELSE NULL END) as actOut1,
		w.arrangeIn2 as in2,
		MIN(case WHEN (((w.arrangeOut1 + w.arrangeIn2)/2) <= MOD(t.checkTime,10000)) and (MOD(t.checkTime,10000) < ((w.arrangeIn2 + w.arrangeOut2)/2))  THEN  MOD(t.checkTime,10000) ELSE NULL END) as actIn2,
		w.arrangeOut2 as out2,
		MAX(case WHEN (((w.arrangeIn2 + w.arrangeOut2)/2) <= MOD(t.checkTime,10000)) and (MOD(t.checkTime,10000) < ((w.arrangeOut2 + "2400")/2))  THEN MOD(t.checkTime,10000) ELSE NULL END) as actOut2
		from `Work` w
		LEFT JOIN `Order` o on w.workId = o.workId
		LEFT JOIN `User` u on o.userId = u.userId
		left JOIN `Time` t on u.userId = t.userId and FLOOR(o.workDate) = FLOOR(t.checkTime/10000)
		GROUP BY u.userId,o.workDate
) tmp
order by userId,workDate