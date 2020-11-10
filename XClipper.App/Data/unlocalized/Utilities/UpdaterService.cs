﻿using Newtonsoft.Json;
using RestSharp;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net.Http;
using System.Reflection;
using System.Threading.Tasks;
using static Components.Constants;

#nullable enable

namespace Components
{
    public class UpdaterService : IUpdater
    {
        public void Check(Action<bool, ReleaseItem?>? block)
        {
            var client = new RestClient();
            var request = new RestRequest(GITHUB_RELEASE_URI, Method.GET);
            client.ExecuteAsync(request, (response) =>
            {
                int appVersion = Assembly.GetExecutingAssembly().GetName().Version.ToString().Replace(".", "").ToInt(); // eg: 1000

                List<ReleaseItem>? releases = JsonConvert.DeserializeObject<List<ReleaseItem>>(response.Content);

                ReleaseItem? newRelease = releases?.FirstOrNull(c => c.GetVersion() > appVersion).Value;
                block?.Invoke(newRelease != null, newRelease);
            });
        }

        public void Launch()
        {
            Process.Start(ApplicationWebsite);
        }
    }
}
