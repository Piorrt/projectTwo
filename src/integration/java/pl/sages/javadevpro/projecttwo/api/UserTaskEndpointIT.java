package pl.sages.javadevpro.projecttwo.api;

import pl.sages.javadevpro.projecttwo.BaseIT;
//import pl.sages.javadevpro.projecttwo.api.usertask.UserTaskRequest;
//import pl.sages.javadevpro.projecttwo.domain.UserTaskService;


class UserTaskEndpointIT extends BaseIT {

//    @Autowired
//    UserTaskService userTaskService;
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    TaskBlueprintService taskBlueprintService;
//
//    @Autowired
//    TaskBlueprintDtoMapper taskBlueprintDtoMapper;
//
//    @Test
//    void user_should_not_be_able_to_assign_task() {
//        //given
//        User user = TestUserFactory.createStudent();
//        userService.save(user);
//        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createWithValidUrl("https://github.com/Piorrt/projectOne");
//        taskBlueprintService.save(taskBlueprint);
//        UserTaskRequest userTaskRequest = new UserTaskRequest(user.getId(), taskBlueprint.getId());
//        String token = getAccessTokenForUser(user.getEmail(), user.getPassword());
//
//        //when
//        ResponseEntity<MessageResponse> response = callAssignTask(userTaskRequest, token);
//
//        //then
//        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//    }
//
//    @Test
//    void admin_should_be_able_to_assign_task_to_user() {
//        User user = TestUserFactory.createStudent();
//        userService.save(user);
//        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createWithValidUrl("https://github.com/Piorrt/projectOne");
//        taskBlueprintService.save(taskBlueprint);
//        UserTaskRequest userTaskRequest = new UserTaskRequest(user.getId(), taskBlueprint.getId());
//        String token = getTokenForAdmin();
//
//        //when
//        ResponseEntity<MessageResponse> response = callAssignTask(userTaskRequest, token);
//        MessageResponse messageResponse = response.getBody();
//
//        //then
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//        Assertions.assertNotNull(messageResponse);
//        Assertions.assertEquals("OK", messageResponse.getStatus());
//        Assertions.assertEquals("Task assigned to user", messageResponse.getMessage());
//
//
//    }
//
//    @Test
//    void admin_should_get_conflict_response_when_trying_to_assign_the_same_task_twice(){
//        User user = TestUserFactory.createStudent();
//        userService.save(user);
//        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createWithValidUrl("https://github.com/Piorrt/projectOne");
//        taskBlueprintService.save(taskBlueprint);
//        UserTaskRequest userTaskRequest = new UserTaskRequest(user.getId(), taskBlueprint.getId());
//        String token = getTokenForAdmin();
//
//        callAssignTask(userTaskRequest, token);
//
//        //when
//        ResponseEntity<MessageResponse> response2 = callAssignTask(userTaskRequest, token);
//
//        //then
//        Assertions.assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
//    }
//
//    @Test
//    void admin_should_get_404_response_code_when_trying_to_add_task_to_not_existing_user() {
//        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createWithValidUrl("https://github.com/Piorrt/projectOne");
//        taskBlueprintService.save(taskBlueprint);
//        UserTaskRequest userTaskRequest = new UserTaskRequest("fakeId", taskBlueprint.getId());
//        String token = getTokenForAdmin();
//
//        //when
//        ResponseEntity<MessageResponse> response = callAssignTask(userTaskRequest, token);
//
//        //then
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    void student_should_be_able_to_take_list_of_files_assigned_to_user_task() {
//        User user = TestUserFactory.createStudent();
//        User savedUser = userService.save(user);
//        TaskBlueprint taskBlueprint = TestTaskBlueprintFactory.createWithValidUrl("https://github.com/rafal-nowak/task1");
//        taskBlueprintService.save(taskBlueprint);
//        UserTaskRequest assignTaskRequest = new UserTaskRequest(savedUser.getId(), taskBlueprint.getId());
//        String adminToken = getTokenForAdmin();
//        String userToken = getAccessTokenForUser(savedUser.getEmail(), savedUser.getPassword());
//
//        //when
//        ResponseEntity<MessageResponse> responseAssignTask = callAssignTask(assignTaskRequest, adminToken);
//        MessageResponse messageResponse = responseAssignTask.getBody();
//
//        //then
//        Assertions.assertEquals(HttpStatus.OK, responseAssignTask.getStatusCode());
//        Assertions.assertNotNull(messageResponse);
//        Assertions.assertEquals("OK", messageResponse.getStatus());
//        Assertions.assertEquals("Task assigned to user", messageResponse.getMessage());
//
//        //when
//        ResponseEntity<ListOfFilesResponse> responseListOfFiles = callGetFilesAssignedToUserTask(savedUser.getId(), taskBlueprint.getId(), userToken);
//        ListOfFilesResponse listOfFilesResponse = responseListOfFiles.getBody();
//
//        //then
//        ArrayList<String> expectedFileList = new ArrayList<>();
//        expectedFileList.add("src/task.py");
//        Assertions.assertEquals(HttpStatus.OK, responseListOfFiles.getStatusCode());
//        Assertions.assertNotNull(listOfFilesResponse);
//        Assertions.assertEquals("OK", listOfFilesResponse.getStatus());
//        Assertions.assertEquals(expectedFileList, listOfFilesResponse.getFiles());
//
//    }
//
//    private ResponseEntity<ListOfFilesResponse> callGetFilesAssignedToUserTask (String userId, String taskId, String accessToken) {
//        String url = "/usertasks/" + userId + "/" + taskId + "/files";
//        return callHttpMethod(HttpMethod.GET,
//                url,
//                accessToken,
//                null,
//                ListOfFilesResponse.class);
//    }
//
//    private ResponseEntity<MessageResponse> callAssignTask(UserTaskRequest body, String accessToken) {
//        return callHttpMethod(HttpMethod.POST,
//                "/usertasks/assign",
//                accessToken,
//                body,
//                MessageResponse.class);
//    }
//
//    @SneakyThrows
//    @AfterAll
//    private static void cleanUpFolders(){
//        File file = new File("testRepo");
//        FileUtils.cleanDirectory(file);
//    }

}
