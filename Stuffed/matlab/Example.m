%{
	This file is part of Stuffed.

	Stuffed is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	Stuffed is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with Stuffed.  If not, see <http://www.gnu.org/licenses/>.
 	
 	File name: 	Example.m
	Created:	March 24th, 2015
	Author:	Rob Lyon

	Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
	Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
        		or <http://www.jb.man.ac.uk>
 	
 	+--- OVERVIEW---+
 	
    The purpose of this script is to show how the stuffed framework can
    be used with MatLab.

%}

% First clear any previous variables.
clear;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                        CUSTOMIZABLE VARIABLES                           %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%% File / Resource paths %%%%%%%%%%

	% Please modify these variables as appropriate.
	root       = '/home/rob/workspace/Stuffed/';
	jarFile1   =  strcat( root , 'dist/Stuffed.jar');
	logPath    =  strcat( root , 'dist/LogOutput.txt');
	resultPath =  strcat( root , 'dist/ClassificationResults.txt');
	
%%%%%%%%%% Candidate input files %%%%%%%%%%

	% The file to be sampled.
    data_file  = strcat( root ,'data/magic04.data.arff');

    % The machine learning model files.
    trainSetPath = strcat( root ,  'test/Train.arff');
    testSetPath  = strcat( root ,  'test/Test.arff');
    
    % Absolute candidate numbers (+/-) in the file
    pos  = 36499;
    neg  = 93565;
    
    runs = 5; % Total test runs.
    
    algorithm = 1;

%%%%%%%%%% Please don't change these variables %%%%%%%%%%

	algorithms = {
	    'J48';
	    'MLP';
	    'NB';
	    'SVM';
	    'HTREE';
	    'GHVFDT'};
	    
	suffix     = '.csv';
	classIndex = -1;

	results         = zeros([runs,4]);    % Stores confusion matrix results.
	aggregate_confusion_matrix = zeros(2);% Stores confusion matrix for each run.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                           EXPERIMENT SETUP                              %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	% Prepare log file and delete old files.
	delete(logPath);
	delete(strcat( root , 'test/*.*'));
	
	% Add Java code to the Matlab class path.
	javaaddpath(jarFile1);
	
	% Create a new sampling object.
	sampler = cs.man.ac.uk.sample.Sampler(logPath,false);
	builder = cs.man.ac.uk.mvc.ClassifierBuilder(logPath,true);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                 SAMPLE                                  %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	%{
	    J48     = 1
		MLP     = 2
		NB      = 3
		SVM     = 4
		HTREE   = 5
		GHVFDT  = 6
	%}
	    
	    negTrainSamples = 200;
	    posTrainSamples = 200;
	    trainSetBalance = 1.0;  % Equal training set balance.
	    labelling       = 1.0;  % All data labelled. 
	    testSetBalance  = 0.5;
        
        for n = 1:runs  % For the specified number of samplings.
            
            sampler.load(data_file, classIndex);
            output = sampler.sampleToARFF(trainSetPath, testSetPath, negTrainSamples, posTrainSamples, trainSetBalance, testSetBalance, labelling);
            result = output(1);

            fprintf('Result of sampling: %i run %i \n',result,n);

            % Now build the classifier
            builder.setTrainingSet(trainSetPath);
            builder.setTestSet(testSetPath);
            builder.setAlgorithm(algorithm);
            builder.setOutputFile(logPath);

            % From the predictions we can compute a confusion matrix, which describes how
            % predicted labels were assigned. The matrix has the form:
            %
            %      Actual Class
            %        -     +
            % P    -----------
            % r  - | TN | FN |    TN = Negatives correctly receiving negative label.
            % e    |---------|    FN = Positives incorrectly receiving negative label.
            % d  + | FP | TP |    FP = Negatives incorrectly receiving positive label.
            % .    -----------    TP = Positives correctly receiving positive label.
            %
            % ^
            % |
            % Predicted.
            confusion_matrix = builder.test();
            
            % Sums this results to those previously obtained.
            aggregate_confusion_matrix(1,1) = aggregate_confusion_matrix(1,1) + double(confusion_matrix(1,1));
            aggregate_confusion_matrix(1,2) = aggregate_confusion_matrix(1,2) + double(confusion_matrix(1,2));
            aggregate_confusion_matrix(2,1) = aggregate_confusion_matrix(2,1) + double(confusion_matrix(2,1));
            aggregate_confusion_matrix(2,2) = aggregate_confusion_matrix(2,2) + double(confusion_matrix(2,2));
            
            % results rows = TN , FN, FP, TP
            results(n,1) = double(confusion_matrix(1,1));
            results(n,2) = double(confusion_matrix(1,2));
            results(n,3) = double(confusion_matrix(2,1));
            results(n,4) = double(confusion_matrix(2,2));
            
            % Results per run:
            TN = double(confusion_matrix(1,1));
            FN = double(confusion_matrix(1,2));
            FP = double(confusion_matrix(2,1));
            TP = double(confusion_matrix(2,2));
            
            % Calculate other metrics.
            accuracy = (TP + TN) / (TP + FP + FN + TN);

            precision = (TP) / (TP + FP);

            recall = (TP) / (TP + FN);

            specificity = (TN) / (FP+TN);

            negativePredictiveValue = (TN) / (FN + TN);

            matthewsCorrelation = ((TP * TN) - (FP * FN)) / sqrt((TP+FP) * (TP+FN) * (TN+FP) * (TN+FN));

            fScore = 2 * ((precision * recall) / (precision + recall));

            % Kappa = (totalAccuracy - randomAccuracy) / (1 - randomAccuracy)
            %
            % where,
            %
            % totalAccuracy = (TP + TN) / (TP + TN + FP + FN)
            %
            % and
            %
            % randomAccuracy = (TN + FP) * (TN + FN) + (FN + TP) * (FP + TP) / (Total*Total).
            total     = TP + TN + FP + FN;
            totalAcc  = (TP + TN) / (TP + TN + FP + FN);
            randomAcc =  (((TN + FP) * (TN + FN)) + ((FN + TP) * (FP + TP))) / (total*total);
            kappa = (totalAcc - randomAcc) / (1 - randomAcc);

            gmean = sqrt( ( TP /( TP + FN ) ) * ( TN / ( TN + FP ) ) );
            
            fprintf('\n****************************************\n')
            fprintf('\nPerformance per run.\n')
            fprintf('TN : \t\t%i\n', TN)
            fprintf('FN : \t\t%i\n', FN)
            fprintf('FP : \t\t%i\n', FP)
            fprintf('TP : \t\t%i\n', TP)
            fprintf('Accuracy:\t%f\n', accuracy)
            fprintf('Precision:\t%f\n', precision)
            fprintf('Recall:\t\t%f\n', recall)
            fprintf('Specificity:\t%f\n', specificity)
            fprintf('NPV:\t\t%f\n', negativePredictiveValue)
            fprintf('MCC:\t\t%f\n', matthewsCorrelation)
            fprintf('F-Score:\t%f\n', fScore)
            fprintf('Kappa:\t\t%f\n', kappa)
            fprintf('G-Mean:\t\t%f\n', gmean)
            
            % Clean up aftr each run.
            delete(strcat( root , 'test/*.*'));
        
        end
	    
        avgPerformance = aggregate_confusion_matrix / runs;
        
        % Results per run:
        TN = double(confusion_matrix(1,1));
        FN = double(confusion_matrix(1,2));
        FP = double(confusion_matrix(2,1));
        TP = double(confusion_matrix(2,2));
            
        % Calculate other metrics.
        accuracy = (TP + TN) / (TP + FP + FN + TN);
        precision = (TP) / (TP + FP);
        recall = (TP) / (TP + FN);
        specificity = (TN) / (FP+TN);
        negativePredictiveValue = (TN) / (FN + TN);
        matthewsCorrelation = ((TP * TN) - (FP * FN)) / sqrt((TP+FP) * (TP+FN) * (TN+FP) * (TN+FN));
        fScore = 2 * ((precision * recall) / (precision + recall));

        % Kappa = (totalAccuracy - randomAccuracy) / (1 - randomAccuracy)
        %
        % where,
        %
        % totalAccuracy = (TP + TN) / (TP + TN + FP + FN)
        %
        % and
        %
        % randomAccuracy = (TN + FP) * (TN + FN) + (FN + TP) * (FP + TP) / (Total*Total).
        total     = TP + TN + FP + FN;
        totalAcc  = (TP + TN) / (TP + TN + FP + FN);
        randomAcc =  (((TN + FP) * (TN + FN)) + ((FN + TP) * (FP + TP))) / (total*total);
        kappa = (totalAcc - randomAcc) / (1 - randomAcc);

        gmean = sqrt( ( TP /( TP + FN ) ) * ( TN / ( TN + FP ) ) );
            
        fprintf('\n****************************************\n')
        fprintf('\nAVG. Performance.\n')
        fprintf('TN : \t\t%i\n', TN)
        fprintf('FN : \t\t%i\n', FN)
        fprintf('FP : \t\t%i\n', FP)
        fprintf('TP : \t\t%i\n', TP)
        fprintf('Accuracy:\t%f\n', accuracy)
        fprintf('Precision:\t%f\n', precision)
        fprintf('Recall:\t\t%f\n', recall)
        fprintf('Specificity:\t%f\n', specificity)
        fprintf('NPV:\t\t%f\n', negativePredictiveValue)
        fprintf('MCC:\t\t%f\n', matthewsCorrelation)
        fprintf('F-Score:\t%f\n', fScore)
        fprintf('Kappa:\t\t%f\n', kappa)
        fprintf('G-Mean:\t\t%f\n', gmean)