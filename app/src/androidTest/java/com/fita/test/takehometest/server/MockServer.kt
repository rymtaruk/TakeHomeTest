package com.fita.test.takehometest.server

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest


class MockServer {
    //Valid Response
    class ResponseDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(200).setBody(
                "{\n" +
                        "    \"resultCount\": 3,\n" +
                        "    \"results\": [\n" +
                        "        {\n" +
                        "            \"artistName\": \"Dewa 19\",\n" +
                        "            \"collectionName\": \"The Best of Dewa 19\",\n" +
                        "            \"trackName\": \"Kangen\",\n" +
                        "            \"previewUrl\": \"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview115/v4/2a/2c/00/2a2c00ef-d8c8-41da-6999-b504c3f0e4a0/mzaf_1875372919675949161.plus.aac.p.m4a\",\n" +
                        "            \"artworkUrl100\": \"https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/3b/ec/30/3bec30fc-5686-1c00-6829-31970f967fd0/Dewa19.jpg/100x100bb.jpg\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"artistName\": \"Dewa 19\",\n" +
                        "            \"collectionName\": \"Dewa 19\",\n" +
                        "            \"trackName\": \"Swear\",\n" +
                        "            \"previewUrl\": \"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/3f/d2/eb/3fd2eb4e-4ed7-78d9-e411-c12eeee24c58/mzaf_11287445382845104820.plus.aac.p.m4a\",\n" +
                        "            \"artworkUrl100\": \"https://is3-ssl.mzstatic.com/image/thumb/Music112/v4/36/f4/ad/36f4ad60-fd8f-cd88-da84-7979bce079f9/Dewa_19.jpg/100x100bb.jpg\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"artistName\": \"Dewa 19\",\n" +
                        "            \"collectionName\": \"The Best of Dewa 19\",\n" +
                        "            \"trackName\": \"Cinta 'Kan Membawamu Kembali\",\n" +
                        "            \"previewUrl\": \"https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/67/80/56/67805626-221d-8aec-07dd-a3b94e60e65e/mzaf_2602186213288694935.plus.aac.p.m4a\",\n" +
                        "            \"artworkUrl100\": \"https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/3b/ec/30/3bec30fc-5686-1c00-6829-31970f967fd0/Dewa19.jpg/100x100bb.jpg\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}"
            )
        }
    }

    //Error Response
    class ErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400)
        }
    }
}