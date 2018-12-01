package test_funzionali;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ChooseVideoTest.class,
	SearchWord.class,
	SearchIntervalTime.class,
	SearchCombined.class
})
public class AllTests {
}
