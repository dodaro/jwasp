# use
#   $ make  
# to test jwasp

BINARY = java -jar ./jwasp.jar

TESTS_DIR = tests

TESTS_TESTER = $(TESTS_DIR)/pyregtest.py

TESTS_COMMAND_AllAnswerSets = $(BINARY) -n 0
TESTS_COMMAND_gringo = gringo | $(BINARY) -n 0
TESTS_COMMAND_SatModel = $(BINARY)
TESTS_COMMAND_WeakConstraints = $(BINARY) -n 0

TESTS_CHECKER_AllAnswerSets = $(TESTS_DIR)/allAnswerSets.checker.py
TESTS_CHECKER_WeakConstraints = $(TESTS_DIR)/weakConstraints.checker.py

TESTS_REPORT_text = $(TESTS_DIR)/text.report.py

TESTS_DIR_asp_AllAnswerSetsTight = $(TESTS_DIR)/asp/AllAnswerSets/tight
TESTS_SRC_asp_AllAnswerSetsTight = $(sort $(shell find $(TESTS_DIR_asp_AllAnswerSetsTight) -name '*.test.py'))
TESTS_OUT_asp_AllAnswerSetsTight = $(patsubst %.test.py,%.test.py.text, $(TESTS_SRC_asp_AllAnswerSetsTight))

TESTS_DIR_asp_AllAnswerSetsNonTight = $(TESTS_DIR)/asp/AllAnswerSets/nontight
TESTS_SRC_asp_AllAnswerSetsNonTight = $(sort $(shell find $(TESTS_DIR_asp_AllAnswerSetsNonTight) -name '*.test.py'))
TESTS_OUT_asp_AllAnswerSetsNonTight = $(patsubst %.test.py,%.test.py.text, $(TESTS_SRC_asp_AllAnswerSetsNonTight))

TESTS_DIR_asp_AllAnswerSetsAggregates = $(TESTS_DIR)/asp/AllAnswerSets/aggregates
TESTS_SRC_asp_AllAnswerSetsAggregates = $(sort $(shell find $(TESTS_DIR_asp_AllAnswerSetsAggregates) -name '*.test.py'))
TESTS_OUT_asp_AllAnswerSetsAggregates = $(patsubst %.test.py,%.test.py.text, $(TESTS_SRC_asp_AllAnswerSetsAggregates))

TESTS_DIR_asp_AllAnswerSetsIntensive = $(TESTS_DIR)/asp/AllAnswerSetsIntensive
TESTS_SRC_asp_AllAnswerSetsIntensive = $(sort $(shell find $(TESTS_DIR_asp_AllAnswerSetsIntensive) -name '*.test.py'))
TESTS_OUT_asp_AllAnswerSetsIntensive = $(patsubst %.test.py,%.test.py.text, $(TESTS_SRC_asp_AllAnswerSetsIntensive))

TESTS_DIR_asp_WeakConstraints = $(TESTS_DIR)/asp/weakConstraints
TESTS_SRC_asp_WeakConstraints = $(sort $(shell find $(TESTS_DIR_asp_WeakConstraints) -name '*.test.py'))
TESTS_OUT_asp_WeakConstraints = $(patsubst %.test.py,%.test.py.text, $(TESTS_SRC_asp_WeakConstraints))

TESTS_DIR_asp_gringo = $(TESTS_DIR)/asp/gringo
TESTS_SRC_asp_gringo = $(sort $(shell find $(TESTS_DIR_asp_gringo) -name '*.test.py'))
TESTS_OUT_asp_gringo = $(patsubst %.test.py,%.test.py.text, $(TESTS_SRC_asp_gringo))

#test: tests/asp/gringo tests/asp/AllAnswerSets/tight tests/asp/AllAnswerSets/nontight tests/asp/AllAnswerSets/aggregates tests/asp/weakConstraints
test: tests/asp/AllAnswerSets/tight tests/asp/AllAnswerSets/nontight tests/asp/AllAnswerSets/aggregates 
#tests/asp: tests/asp/gringo tests/asp/AllAnswerSets/tight tests/asp/AllAnswerSets/nontight tests/asp/AllAnswerSets/aggregates tests/asp/weakConstraints

tests/asp/gringo: $(TESTS_OUT_asp_gringo)
tests/asp/AllAnswerSets/tight: $(TESTS_OUT_asp_AllAnswerSetsTight)
tests/asp/AllAnswerSets/nontight: $(TESTS_OUT_asp_AllAnswerSetsNonTight)
tests/asp/AllAnswerSets/aggregates: $(TESTS_OUT_asp_AllAnswerSetsAggregates)
tests/asp/weakConstraints: $(TESTS_OUT_asp_WeakConstraints)

tests/asp/AllAnswerSetsIntensive: $(TESTS_OUT_asp_AllAnswerSetsIntensive)

$(TESTS_OUT_asp_AllAnswerSetsTight):
	@$(TESTS_TESTER) "$(TESTS_COMMAND_AllAnswerSets)" $(patsubst %.test.py.text,%.test.py , $@) $(TESTS_CHECKER_AllAnswerSets) $(TESTS_REPORT_text)

$(TESTS_OUT_asp_AllAnswerSetsNonTight):
	@$(TESTS_TESTER) "$(TESTS_COMMAND_AllAnswerSets)" $(patsubst %.test.py.text,%.test.py , $@) $(TESTS_CHECKER_AllAnswerSets) $(TESTS_REPORT_text)

$(TESTS_OUT_asp_AllAnswerSetsAggregates):
	@$(TESTS_TESTER) "$(TESTS_COMMAND_AllAnswerSets)" $(patsubst %.test.py.text,%.test.py , $@) $(TESTS_CHECKER_AllAnswerSets) $(TESTS_REPORT_text)

$(TESTS_OUT_asp_AllAnswerSetsIntensive):
	@$(TESTS_TESTER) "$(TESTS_COMMAND_AllAnswerSets)" $(patsubst %.test.py.text,%.test.py , $@) $(TESTS_CHECKER_AllAnswerSets) $(TESTS_REPORT_text)

$(TESTS_OUT_asp_WeakConstraints):
	@$(TESTS_TESTER) "$(TESTS_COMMAND_WeakConstraints)" $(patsubst %.test.py.text,%.test.py , $@) $(TESTS_CHECKER_WeakConstraints) $(TESTS_REPORT_text)

$(TESTS_OUT_asp_gringo):
	@$(TESTS_TESTER) "$(TESTS_COMMAND_gringo)" $(patsubst %.test.py.text,%.test.py , $@) $(TESTS_CHECKER_AllAnswerSets) $(TESTS_REPORT_text)
