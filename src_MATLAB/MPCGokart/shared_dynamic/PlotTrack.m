% points = [36.2,52,57.2,53,55,47,41.8;
%           44.933,58.2,53.8,49,44,43,38.33;
%           1.8,0.5,1.8,0.4,0.4,0.4,1.8]';
% close all
% points = [28,35,42,55.2,56,51,42,40;...          %x
%           41,60,43,56,43,40,44,31; ...    %y
%           1.8,1.4,1,1.5,0.5,0.5,0.5,1.4]';
points = [18,22,35,42,55.2,60,51,42,40,30,22;...          %x
          41,52,55,57,56,43,40,42,31,35,34; ...    %y
          2.5,2.5,2.5,2.5,2.5,2.3,2.3,2.3,2.3,2.5,2.5]';
points2 = [18,22,35,42,55.2,60,51,42,40,30,22;...          %x
          41,52,55,57,56,43,40,42,31,35,34; ...    %y
          2.5,2.5,2.5,2.5,2.5,2.3,2.3,2.3,2.3,2.5,2.5]';
points2(:,1)=points2(:,1)+20;
% points = [8,7,5,7,10,10,11,18,25,35,40,39,40,41,37,35,30,25,20,15;...          %x
%           10,25,35,45,55,61,70,72,70,65,60,50,42,30,18,12,13,12,10,8; ...    %y
%           4,4,4,4,4,4,4,4,3,3,2,2,2,3,2,2,2,2,3,3.5]';
[leftline,middleline,rightline] = drawTrack(points(:,1:2),points(:,3));
[leftline2,middleline2,rightline2] = drawTrack(points2(:,1:2),points2(:,3));
figure
hold on
plot(leftline(:,1),leftline(:,2),'b')
plot(middleline(:,1),middleline(:,2),'r')
plot(rightline(:,1),rightline(:,2),'b')
points1 = [points;points(1,1),points(1,2),points(1,3)];
plot(points1(:,1),points1(:,2),'g*-')
plot(leftline2(:,1),leftline2(:,2),'b')
plot(middleline2(:,1),middleline2(:,2),'r')
plot(rightline2(:,1),rightline2(:,2),'b')
points3 = [points2;points2(1,1),points2(1,2),points2(1,3)];
plot(points3(:,1),points3(:,2),'g*-')
xlabel('x')
ylabel('y')
grid on
