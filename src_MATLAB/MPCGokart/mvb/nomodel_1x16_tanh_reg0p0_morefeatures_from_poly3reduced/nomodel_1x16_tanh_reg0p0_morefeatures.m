function [ACCX,ACCY,ACCROTZ] = nomodel_1x16_tanh_reg0p0_morefeatures(VX,VY,VTHETA,BETA,AB,TV, param)    
    w1 = [0.49538067 1.2183245 0.19999585 -0.11281382 1.2943174 -0.15829684 -0.069545574 -0.46058473 0.13736455 -0.3917594 0.66732794 0.05871298 -0.053748365 -0.23369005 0.6489759 -1.2756623;-0.27391025 -0.37198615 -0.11295798 0.4537286 -1.9291372 -0.07840416 0.34948584 -0.2112467 -0.4938315 0.74350494 0.40328568 0.16925366 0.4514123 0.6360026 -1.0896482 -0.3632911;0.07489171 -0.3220183 -0.24364445 -0.019766979 -0.18343444 -0.19329223 0.105351575 -0.05660534 -0.15208456 -0.03511384 -0.14307977 0.4163255 0.09335616 0.3482184 0.33132142 -0.39226237;-0.13506435 -0.02482712 0.015724346 -0.18060496 -0.014811136 -0.56950855 -0.024069268 0.25880986 0.14040133 -0.13140522 -1.1633492 -0.3244436 -0.7552644 0.50885284 0.015992163 0.1027219;0.025497634 -0.5954936 0.08060617 -0.17408238 0.118879646 -0.33901855 0.27034274 -0.0039701383 -0.07954468 0.0661476 -0.29692337 -0.12026644 0.26692098 0.036142305 -0.2116316 0.4531094;-0.15906154 0.15761036 0.11734105 -0.21949917 0.17820838 -0.12651157 0.013629313 0.042715173 0.39236268 -0.16519968 -0.42330226 0.28284094 -0.14271854 -0.29904425 -0.1078484 0.39507696;-0.14219244 0.35192662 -0.27604502 -0.15105769 0.37763253 -0.6998683 -0.18897249 0.697226 -0.09954345 -0.611142 0.6735476 0.12726799 0.6961578 0.501477 -1.0053557 -0.35295552;0.28466788 0.13651907 0.037475333 0.14515747 0.09439117 -0.07937025 -0.73731524 -0.80957204 -0.020166328 0.34267452 0.71016026 0.034808103 0.09880167 0.5774118 -0.17789689 -0.5951187;-0.29716375 -0.35291478 -0.07894393 -0.24229182 0.70507663 0.19668859 0.2849479 0.44914845 0.2714664 -0.29554948 -0.64503133 -0.41732317 -0.72725046 -0.51985437 0.095382005 1.315553;0.02492636 0.4583373 -0.14866945 0.116082184 -0.14217336 0.23960188 -0.6807154 -0.16685483 0.3513271 0.49840364 0.1471336 0.26770586 0.026019195 -0.26551175 -0.031059891 0.06880546;-0.08166283 0.2314021 0.19257009 0.20567279 0.066150606 -0.19420202 0.06549178 0.14721538 0.21372537 0.47285366 0.60544974 -0.037136883 0.07635486 -0.2476693 -0.09489824 0.12749135;-0.002068766 0.6809338 -0.14388062 -0.2691932 -0.43520382 -0.37785247 -0.2890718 -0.14314395 0.13661477 0.58458716 0.11494407 0.11023067 0.95693606 -0.33332756 0.46172518 -0.06324509;0.0023642874 0.050468475 -0.03209874 0.4082065 -0.17424686 -0.31293413 0.22902332 -0.12724423 -0.043110553 -0.43440485 0.14734855 -0.21022446 -0.36585584 -0.17714608 0.1784281 0.060079053;-0.0001756337 0.31944105 -0.032642823 -0.18990171 -0.11792703 -0.194509 0.086592205 -0.38088608 -0.04161437 -0.099493615 -0.16576812 -0.19620652 0.13206597 -0.258938 -0.079426736 0.18555857;0.16627884 -0.19798225 -0.046280675 0.027273888 0.25017044 0.07872338 -0.20425108 0.0013382848 -0.11915951 0.20623605 0.06574756 -0.15852109 -0.5370085 0.36837846 0.2589514 -0.12771197;-0.08195502 -0.015737414 0.20362808 0.04486153 -0.30330607 0.3827455 0.5699838 0.08154063 0.17228974 0.28084585 -0.19043176 -0.042923 -1.2940207 -0.36384258 -0.40665773 1.0841513;0.15872104 0.01911219 0.15224166 0.011735475 1.0715337 -0.1638201 -1.1815152 0.46425587 0.48916608 0.10109588 0.7753074 -0.10039607 0.29630387 0.05408425 0.58137375 -0.89224994;0.101125196 -0.043906026 -0.0055607795 -0.024747929 -0.7712238 1.0175672 0.5022102 -0.30015275 -0.27598011 -0.38189384 -0.9080364 0.21436402 -0.47824442 -0.40905434 1.2231401 0.058225058;-0.0661424 0.0636204 0.0906805 -0.13820125 0.12816638 -0.20384516 0.9049343 -0.008802156 -0.30139807 -0.42021018 0.43360513 -0.1620678 -0.3316689 0.2960952 -0.17172518 -0.06467937;0.10987102 0.6871951 -0.11850215 -0.12810008 -0.05995652 0.13372979 -0.29468673 -0.1534985 -0.18697399 -0.38493568 -0.67374825 0.0131293 0.070396505 0.02993021 0.21589555 -0.12260293;-0.034539767 -0.15909362 0.00735221 0.18889323 -0.3552553 -0.12118389 0.30599248 -0.16714565 -0.020029735 0.14187533 -0.21416084 0.23028685 -0.25749108 0.2616717 -0.3418871 0.109791644;-0.10380048 -0.10575352 0.16194184 -0.16149019 0.15649344 0.37525192 0.16582802 0.106358685 0.089883804 -0.10080545 0.15691659 0.12144452 0.20696703 0.24475044 -0.37802652 0.24665199;0.057320774 0.29563954 -0.082168624 -0.074148454 0.32989946 0.18915692 -0.10854077 0.047595747 -0.05795999 -0.09365392 0.00029583796 -0.24417242 0.38698754 -0.18614241 0.3360685 -0.066193946;0.01514463 0.90644664 0.08764014 -0.043068215 -1.3712058 0.14712997 -1.6535807 -0.22675188 0.18964478 -0.18263726 0.3493026 0.06483542 -0.65967625 0.07104769 0.7595309 -0.59750795;-0.09381468 -0.23987041 0.09050478 0.8251767 0.68861556 0.051421523 1.3773468 -0.09791375 0.20858264 1.4544107 -0.38864407 -0.08667724 0.35459858 0.14473054 0.372188 -0.2049467;-0.03234947 -0.22122584 -0.009479642 -0.037517726 -0.0706483 0.0032158922 0.002768638 0.14733543 0.004615508 -0.2215139 -0.25417748 0.05696866 -0.19102132 -0.18979608 0.020112291 0.16816644;0.03440836 0.31442755 0.089117005 -0.30290097 -0.26509932 -0.25926355 -0.5672754 -0.0780305 0.19209191 -0.5894693 -0.6951952 -0.0027446072 -0.07872026 -0.14241786 0.37139055 -0.2254598];
    b1 = [0.394077 0.031071633 -0.8851068 0.04609992 0.22951351 -0.6200846 0.18027012 -0.37347537 1.0404924 1.3570764 -0.30827317 -1.0199556 -0.9982522 0.4091864 0.6547088 -0.30455166];
    w2 = [-0.12332308 -2.403174 0.9196337;0.065599695 -0.20408627 0.59211934;-2.158928 -0.15137485 1.4377697;-0.10989926 1.0611796 -0.30342114;-0.012403018 1.0802302 -0.1783204;-0.13676263 -0.1516446 0.9223987;-0.16455653 -0.3324862 0.88293725;-0.3811317 -1.0494457 0.88758665;1.3297278 -0.37763098 1.1650852;0.0076365466 0.68811125 0.21484624;0.03860743 -0.12192126 -0.60877544;0.8173614 -0.042730153 -0.12733124;0.021777766 -0.03522648 -0.89674443;-0.26883727 0.10540213 -0.9369726;-0.035940595 0.23788008 0.84050924;-0.024399836 0.34471807 0.7035583];
    b2 = [-2.0316048 -0.09932586 -0.28122345];
    means = [0.0006739826858158322 -0.04802685383934488 0.06560439433747117 1.373170976826475 0.7617875788004159 0.00824770448780333 0.03187135437735826 -0.19631195879670152 -0.059164368850320524 0.4213669524546223 -0.2317972077113321 -0.0075517562606167935 0.30753697144650266 0.24997696696256436 0.04121559965423868 0.3681228086413301 -1.1304472770254048 -0.22996882531985796 2.830314122401715 -1.0850356721033614 0.22140526576970496 0.10322624822347427 0.03662337538062741 -0.035928699026757015 0.026297031571554143 -0.00011789782511803328 -0.0682334439544658];
    stds = [0.3073995994274385 0.5524821241332127 0.837672127818587 4.343141970106426 0.2510784486857777 0.14978557053838454 1.6143434321121914 2.706649563739692 0.616404092345298 4.251815853244271 2.6490140417448003 0.4547007200691509 0.6347497015012956 0.4690375873163249 0.04580274658393033 10.242007534778573 16.745791698795607 3.2498046086031787 29.274345670034155 15.725026610310815 3.972996150420444 0.14699888695062427 0.7940023371395283 1.0611067890823749 0.8483148589544969 0.024391213816947373 1.195605815618373];

    input = [VY,VTHETA,AB,1/2.0^AB,cos(BETA/0.44*pi/2.0),tan(AB/6.7*pi/3.0),(VX) * (VY),(VX) * (VTHETA),(VX) * (BETA),(VX) * (AB),(VX) * (TV),(VY) * (VTHETA),(VTHETA) * (VTHETA),(VTHETA) * (TV),(BETA) * (BETA),(VX) * (VX) * (VY),(VX) * (VX) * (VTHETA),(VX) * (VX) * (BETA),(VX) * (VX) * (AB),(VX) * (VX) * (TV),(VX) * (VTHETA) * (AB),(VX) * (BETA) * (BETA),(VX) * (BETA) * (AB),(VY) * (VY) * (VTHETA),(VY) * (VTHETA) * (VTHETA),(VY) * (BETA) * (BETA),(VTHETA) * (VTHETA) * (VTHETA)];

    normed_input = (input - means) ./ stds;

    h1 = tanh(normed_input * w1 + b1);
    disturbance = h1 * w2 + b2;
    
    ACCX = disturbance(1);
    ACCY = disturbance(2);
    ACCROTZ = disturbance(3);
end
