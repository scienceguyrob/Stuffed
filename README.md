******************************************************************************************

# Stuffed

Stuffed Framework - Useful for testing algorithms on unlabelled data streams.

Author: Rob Lyon, School of Computer Science & Jodrell Bank Centre for Astrophysics,
		University of Manchester, Kilburn Building, Oxford Road, Manchester M13 9PL.

Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
Web:		http://www.scienceguyrob.com or http://www.cs.manchester.ac.uk
			or alternatively http://www.jb.man.ac.uk
******************************************************************************************

1. Overview

	Stuffed is a wrapper for WEKA and MOA classification algorithms, which enables testing
	and evaluation on unlabelled data streams. This is (or was last I checked) hard to achieve
	with MOA. Stuffed makes this possible by using custom sampling methods to sample large
	data sets so that they can contain:
	
		- Varied levels of class balance in both test and training sets.
		- Varied levels of labelling in the test data streams.
		
	The custom sampling method produces meta data with each sampling, that allows stream
	classifier predictions to be evaluated on unlabelled data. For instance, if a data
	item in the stream is unlabelled (?), typical evaluation mechanisms would not evaluate
	classifier performance on this example. However since Stuffed keeps meta data at hand,
	it is possible to evaluate the label assigned by a classifier to each unlabelled instance.
	
	Stuffed is only designed to work on binary classification problems. It can be used
	to gather statistics on classifier performance, is easily extensible, and can be
	used with other tools such as MatLab.
		
	So far Stuffed has been used to perform experiments for two papers:
	
	R. J. Lyon, J. M. Brooke, J. D. Knowles, B. W. Stappers.  A Study on Classification
	in Imbalanced and Partially-Labelled Data Streams, in International Conference on
	Systems, Man, and Cybernetics (SMC), pages 1506-1511, 2013, IEEE.
	
	R. J. Lyon, J. M. Brooke, J. D. Knowles, B. W. Stappers. Hellinger Distance Trees
	for Imbalanced Streams, In 22nd International Conference on Pattern Recognition,
	pages 1969-1974, Stockholm, Sweden, 2014, IEEE.
	
	If you use Stuffed please use the citations below.

2. Use
	
	The algorithm is designed to work directly with both the MOA stream test framework and
	WEKA. It is a wrapper API, thus is not meant to be executed as an application. Rather you
	incorporate it directly into your code projects, to be extended, refined and improved.
	
	The code comes with examples of how it can be executed which speaks for themselves. Also
	check the user manual for more information.
	
3. Citing our work

	Please use the following citation if you make use of this algorithm:
	
	@inproceedings{Lyon:2014:jk,
	author    = {{Lyon}, R.~J. and {Knowles}, J.~D. and {Brooke}, J.~M. and {Stappers}, B.~W.},
	title     = {{Hellinger Distance Trees for Imbalanced Streams}},
	booktitle = {22nd IEEE International Conference on Pattern Recognition}, 
	series    = {ICPR '14},
	year      = {2014},
	month     = {August},
	pages     = {1969-1974},
	location  = {Stockholm, Sweden},
	publisher = {IEEE}
	}
	
	@inproceedings{Lyon:2013:jk,
	author    = {{Lyon}, R.~J. and {Knowles}, J.~D. and {Brooke}, J.~M. and {Stappers}, B.~W.},
	title     = {{A Study on Classification in Imbalanced and Partially-Labelled Data Streams}},
	booktitle = {International Conference on Systems, Man, and Cybernetics}, 
	series    = {SMC '13},
	year      = {2013},
	month     = {October},
	pages     = {1506-1511},
	location  = {Manchester, United Kingdom},
	publisher = {IEEE}
	}
	
4. Acknowledgements

	This work was supported by grant EP/I028099/1 for the University of Manchester Centre for
	Doctoral Training in Computer Science, from the UK Engineering and Physical Sciences Research
	Council (EPSRC).
