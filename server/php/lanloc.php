<?php

$servername = "127.0.0.1:3306";
$username = "LanLoc";
$password = "123j456b";
$database = "lanloc";

$con = new mysqli($servername, $username, $password, $database);

if ($con->connect_error) {
        die("Connection failed: " . $con->connect_error);
        echo "Failed";
} 

$method = null;
if (isset($_POST["method"])){
        $method = $_POST["method"];
}
//if ($method === "getUserById") {
//      $id = $_POST["id"];
//      getUserById($con, $id);
//} else if ($method === "getUserByAndroidId") {
//      $android_id = $_POST["android_id"];
//      getUserByAndroidId($con, $android_id);
//} else if ($method === "getPositionId") {
//      $latitude = $_POST["latitude"];
//      $longitude = $_POST["longitude"];
//      getPositionId($con, $latitude, $longitude);
//} else if ($method == "getPositionById") {
//      $id = $_POST["id"];
//      getPositionById($con, $id);
//}  else 
if ($method === "addRecord") {
        $android_id = $_POST["android_id"];
        $latitude = $_POST["latitude"];
        $longitude = $_POST["longitude"];
        $audio = $_POST["audio"];
        addRecord($con, $android_id, $latitude, $longitude, $audio);
} else if ($method === "voteRecordUp") {
        $record_id = $_POST["record"];
        $android_id = $_POST["android_id"];
        voteRecordUp($con, $record_id, $android_id);
} else if ($method === "voteRecordDown") {
        $record_id = $_POST["record"];
        $android_id = $_POST["android_id"];
        voteRecordDown($con, $record_id, $android_id);
//} else if ($method === "getAllPositionsInRange") {
//      $latitude = $_POST["latitude"];
//        $longitude = $_POST["longitude"];
//      $distance = $_POST["distance"];
//      getAllPositionsInRange($con, $latitude, $longitude, $distance);
//} else if ($method === "getRecordsForPositions") {
//      $positions = $_POST["positions"];
//      getRecordsForPositions($con, $positions, "default");
} else if ($method === "getRecordsAroundPosition") {
        $latitude = $_POST["latitude"];
        $longitude = $_POST["longitude"];
        $distance = $_POST["distance"];
        $order = $_POST["order"];
        getRecordsAroundPosition($con, $latitude, $longitude, $distance, $order);
}

/*function getUserById($con, $id) {
        $sql = "SELECT * FROM user WHERE id = '" . $id . "'";
        $result = $con->query($sql);
        $result_array = array();

        if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                        $result_array[] = $row;
                }
        } 
        echo json_encode($result_array);
}*/

function getUserByAndroidId($con, $android_id) {
        $sql = "SELECT * FROM user WHERE android_id = '" . $android_id . "'";
        $result = $con->query($sql);
        $result_array = array();

        if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                        return $row["id"];
                }
        }

        addNewUser($con, $android_id);
        return getUserByAndroidId($con, $android_id);
}

function addNewUser($con, $android_id) {
        $sql = "INSERT INTO user (android_id) VALUES ('" . $android_id .  "')";
        if($con->query($sql) === FALSE) {
                echo "failure";
        }
}

/*function getPositionId($con, $lat, $lon) {
        $sql = "SELECT * FROM positions WHERE latitude  = '" . $lat . "' AND longitude = '" . $lon . "'";
        $result = $con->query($sql);
        $result_array = array();

        if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                        $result_array[] = $row;
                }
        }  else {
                addPosition($con, $lat, $lon);
                getPositionId($con, $lat, $lon);
        }

        echo json_encode($result_array);
}*/

/*function getPositionById($con, $id) {
        $sql = "SELECT * FROM positions WHERE id = '" . $id . "'";
        $result = $con->query($sql);
        $result_array = array();

        if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                        $result_array[] = $row;
                }
        }

        echo json_encode($result_array);
}*/

function addPosition($con, $lat, $lon) {
        $sql = "INSERT INTO positions (latitude, longitude) VALUES ('" . $lat .  "', '" . $lon . "')";
        if($con->query($sql) === FALSE) {
                echo "failure";
        }
}

function getPositionByLatLon($con, $latitude, $longitude) {
        $sql = "SELECT * FROM positions WHERE latitude = '" . $latitude . "' AND longitude = '" . $longitude . "'";
        $result = $con->query($sql);

        if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                        return $row["id"];
                }
        }

        addPosition($con, $latitude, $longitude);
        return getPositionByLatLon($con, $latitude, $longitude);
}

function addRecord($con, $android_id, $latitude, $longitude, $audio) {
        $user_id = getUserByAndroidId($con, $android_id);
        $position_id = getPositionByLatLon($con, $latitude, $longitude);

        $sql = "INSERT INTO records (user, position, audio) VALUES ('" . $user_id . "', '" . $position_id . "', '" . $audio . "')";
        if($con->query($sql) === FALSE) {
                echo "failure";
        }
}

function addVoteEntry($con, $record_id, $android_id) {
        $user = getUserStringByAndroidId($con, $android_id);
        $user_id = str_replace("(", "", str_replace(")", "", $user));
        $sql = "INSERT INTO votes (record, user) VALUES ('" . $record_id . "', '" . $user_id . "')";
        if($con->query($sql) === FALSE) {
                echo "failure";
        }
}

function voteRecordUp($con, $record_id, $android_id) {
        $sql = "UPDATE records SET up_votes = up_votes + 1 WHERE id = '" . $record_id . "'";
        if($con->query($sql) === FALSE) {
                echo "failure";
        }

        addVoteEntry($con, $record_id, $android_id);
}

function voteRecordDown($con, $record_id, $android_id) { 
        $sql = "UPDATE records SET down_votes = down_votes + 1 WHERE id = '" . $record_id . "'";
        if($con->query($sql) === FALSE) { 
                echo "failure";
        }

        addVoteEntry($con, $record_id, $android_id);
}

/*function getAllPositionsInRange($con, $current_latitude, $current_longitude, $distance) {
        $sql = "SELECT *,(((acos(sin(('" . $current_latitude . "'*pi()/180)) * sin((dest.latitude*pi()/180))+cos(('" . $current_latitude . "'*pi()/180))*cos((dest.latitude*pi()/180))*cos((('" . $current_longitude . "'-dest.longitude)*pi()/180))))*180/pi())*60*1.1515*1609.344) as distance  FROM positions AS dest HAVING distance < '" . $distance . "' ORDER BY distance ASC LIMIT 100";
        $result = $con->query($sql);
        $result_array = array();

        if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                        $result_array[] = $row;
                }
        } 

        echo json_encode($result_array);
}*/

function getUserStringByAndroidId($con, $android_id) {
        $sql = "SELECT * FROM user WHERE android_id = '" . $android_id . "'";
        $result = $con->query($sql);
        $result_array = array();
        $user_string = "(";

        if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                        $user_string .= $row["id"];
                }
        }

        $user_string .= ")";

        return $user_string;
}

function getRecordsForPositions($con, $positions, $order) {
        if($positions === "()") {
                echo "[]";
        } else {

        $user = getUserStringByAndroidId($con, $_POST["android_id"]);
        if($user == "()") {
                $user ="(0)"; 
        }

        $sql = "SELECT r.id, r.date_time, r.user, r.up_votes, r.down_votes, r.audio, p.latitude, p.longitude, up_votes - down_votes as votes, CASE WHEN r.id IN (SELECT record FROM votes WHERE user IN " . $user . ") THEN 0 ELSE 1 END as isVotable FROM lanloc.records AS r LEFT JOIN positions AS p ON r.position = p.id WHERE position IN " . $positions;
        if(isset($_POST["filterByUser"]) && $_POST["filterByUser"] === "true") {
                $sql .= " AND user IN " . $user;
        }

        if ($order === "date") {
                $sql .= " ORDER BY date_time DESC";
        } else {
                $sql .= " ORDER BY votes DESC";
        }

        $result = $con->query($sql);
        $result_array = array();

        if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                        $result_array[] = $row;
                }
        } 

        echo json_encode($result_array); 
        }
}

function getRecordsAroundPosition($con, $current_latitude, $current_longitude, $distance, $order) {
        $sql = "SELECT *,(((acos(sin(('" . $current_latitude . "'*pi()/180)) * sin((dest.latitude*pi()/180))+cos(('" . $current_latitude . "'*pi()/180))*cos((dest.latitude*pi()/180))*cos((('" . $current_longitude . "'-dest.longitude)*pi()/180))))*180/pi())*60*1.1515*1609.344) as distance  FROM positions AS dest HAVING distance < '" . $distance . "' ORDER BY distance ASC LIMIT 100";
        $result = $con->query($sql);
        $result_array = array();
        $id_string = "(";

        if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                        $result_array[] = $row;
                        $id_string .= $row["id"] . ", ";
                }
        } 

        $id_string .= ")";
        $id_string = str_replace(", )", ")", $id_string);

        getRecordsForPositions($con, $id_string, $order);
}

$con->close();

?>