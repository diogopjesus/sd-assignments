# Check if given file has a line different than the next one
# Usage: python check-log.py <file>
# Example: python check-log.py output.log

import sys
import re

LOG_HEADER = """                             Heist to the Museum - Description of the internal state

MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6
Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD
                   Assault party 1                       Assault party 2                       Museum
           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5
    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT

"""

ORDINARY_THIEF_STATES = ["CONC", "INWA", "ROOM", "OUTW", "COLL"]
MASTER_THIEF_STATES = ["PLAN", "DECI", "ASSE", "WAIT", "PRES"]
ORDINARY_THIEF_SITUATION = ["W", "P"]

LOG_TAIL = "My friends, tonight's effort produced [0-9][0-9] priceless paintings!"


def check_file_lines(file, show_diff=False):
    no_equal_lines = True
    with open(file) as f:
        lines = f.readlines()
        for i in range(8, len(lines) - 4, 2):
            if lines[i] == lines[i + 2] and lines[i + 1] == lines[i + 3]:
                print(
                    "Warning: Status was not updated between lines %d and %d!"
                    % (i + 1, i + 4)
                )
                no_equal_lines = False
    return no_equal_lines


def check_if_file_is_log(file):
    with open(file) as f:
        lines = f.readlines()
        # Check file header
        if "".join(lines[0:8]) != LOG_HEADER:
            print(
                "Error: File %s is not a valid log file! Header failed!" % file,
                file=sys.stderr,
            )
            sys.exit(1)

        # Check file tail
        if not re.match(LOG_TAIL, lines[-1]):
            print(
                "Error: File %s is not a valid log file! Tail failed!" % file,
                file=sys.stderr,
            )
            sys.exit(1)

        # check if log output is well formatted
        for i in range(8, len(lines) - 1, 2):
            line = lines[i].split()
            if line[0] not in MASTER_THIEF_STATES:
                print(
                    "Error: Line %d does not have a master thief state!" % (i + 1),
                    file=sys.stderr,
                )
                sys.exit(1)
            # check if thieves information is well formatted
            for j in range(1, len(line), 3):
                if line[j] not in ORDINARY_THIEF_STATES:
                    print(
                        "Error: Line %d does not have an ordinary thief state!"
                        % (i + 1),
                        file=sys.stderr,
                    )
                    sys.exit(1)
                if line[j + 1] not in ORDINARY_THIEF_SITUATION:
                    print(
                        "Error: Line %d does not have an ordinary thief situation!"
                        % (i + 1),
                        file=sys.stderr,
                    )
                    sys.exit(1)
                if (
                    not line[j + 2].isnumeric()
                    or len(line[j + 2]) > 1
                    or int(line[j + 2]) < 2
                    or int(line[j + 2]) > 6
                ):
                    print(
                        "Error: Line %d does not have a valid ordinary maximum displacement!"
                        % (i + 1),
                        file=sys.stderr,
                    )
                    sys.exit(1)

            ### check if assault status is well formatted ###

            # check first assault party
            line = lines[i + 1].split()
            if not line[0].isnumeric() or len(line[0]) > 1:
                print(
                    "Error: Line %d does not have a valid assault room id!" % (i + 1),
                    file=sys.stderr,
                )
                sys.exit(1)

            for j in range(1, 10, 3):
                if (
                    (not line[j].isnumeric())
                    or len(line[j]) > 1
                    or int(line[j]) < 0
                    or int(line[j]) > 6
                ):
                    print(
                        "Error: Line %d does not have a valid ordinary thief id!"
                        % (i + 1),
                        file=sys.stderr,
                    )
                    sys.exit(1)
                if (
                    not line[j + 1].isnumeric()
                    or len(line[j + 1]) > 2
                    or int(line[j + 1]) < 0
                ):
                    print(
                        "Error: Line %d does not have a valid ordinary thief position!"
                        % (i + 1),
                        file=sys.stderr,
                    )
                    sys.exit(1)
                if (
                    not line[j + 2].isnumeric()
                    or len(line[j + 2]) > 1
                    or int(line[j + 2]) < 0
                    or int(line[j + 2]) > 1
                ):
                    print(
                        "Error: Line %d does not have a valid ordinary thief carrying canvas!"
                        % (i + 1),
                        file=sys.stderr,
                    )
                    sys.exit(1)

            # check second assault party
            if not line[10].isnumeric() or len(line[0]) > 1:
                print(
                    "Error: Line %d does not have a valid assault room id!" % (i + 1),
                    file=sys.stderr,
                )
                sys.exit(1)

            for j in range(11, 20, 3):
                if (
                    (not line[j].isnumeric())
                    or len(line[j]) > 1
                    or int(line[j]) < 0
                    or int(line[j]) > 6
                ):
                    print(
                        "Error: Line %d does not have a valid ordinary thief id!"
                        % (i + 1),
                        file=sys.stderr,
                    )
                    sys.exit(1)
                if (
                    not line[j + 1].isnumeric()
                    or len(line[j + 1]) > 2
                    or int(line[j + 1]) < 0
                ):
                    print(
                        "Error: Line %d does not have a valid ordinary thief position!"
                        % (i + 1),
                        file=sys.stderr,
                    )
                    sys.exit(1)
                if (
                    not line[j + 2].isnumeric()
                    or len(line[j + 2]) > 1
                    or (int(line[j + 2]) < 0 and int(line[j + 2]) > 1)
                ):
                    print(
                        "Error: Line %d does not have a valid ordinary thief carrying canvas!"
                        % (i + 1),
                        file=sys.stderr,
                    )
                    sys.exit(1)

            # check room status
            for j in range(len(line) - 10, len(line), 2):
                if (
                    not line[j].isnumeric()
                    or len(line[j]) > 2
                    or int(line[j]) < 0
                    or int(line[j]) > 16
                ):
                    print(
                        "Error: Line %d does not have a valid number of paintings on a room!"
                        % (i + 1),
                        file=sys.stderr,
                    )
                    sys.exit(1)
                if (
                    not line[j + 1].isnumeric()
                    or len(line[j + 1]) > 2
                    or (int(line[j + 1]) < 15 and int(line[j + 1]) > 30)
                ):
                    print(
                        "Error: Line %d does not have a valid room distance!" % (i + 1),
                        file=sys.stderr,
                    )
                    sys.exit(1)


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python check-log.py <file> [show_differences=True|False]")
        sys.exit(1)
    check_if_file_is_log(sys.argv[1])
    print("File %s formatting correct!" % sys.argv[1])

    # check if file has equal consecutive lines
    if check_file_lines(sys.argv[1]):
        print("File %s does not have equal log lines!" % sys.argv[1])
        sys.exit(0)
