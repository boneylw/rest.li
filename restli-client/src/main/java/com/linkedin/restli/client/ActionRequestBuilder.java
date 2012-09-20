/*
   Copyright (c) 2012 LinkedIn Corp.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

/**
 * $Id: $
 */

package com.linkedin.restli.client;

import java.util.HashMap;
import java.util.Map;

import com.linkedin.data.template.DynamicRecordTemplate;
import com.linkedin.data.template.FieldDef;
import com.linkedin.jersey.api.uri.UriBuilder;
import com.linkedin.restli.common.ResourceSpec;
import com.linkedin.restli.common.RestConstants;
import com.linkedin.restli.internal.client.ActionResponseDecoder;

/**
 * @author Josh Walker
 * @version $Revision: $
 */

public class ActionRequestBuilder<K, V> extends AbstractRequestBuilder<K, V, ActionRequest<V>>
{
  private final Class<V> _elementClass;
  private K _id;
  private String _name;
  private final Map<FieldDef<?> , Object> _actionParams = new HashMap<FieldDef<?>, Object>();

  public ActionRequestBuilder(String baseUriTemplate, Class<V> elementClass, ResourceSpec resourceSpec)
  {
    super(baseUriTemplate, resourceSpec);
    _elementClass = elementClass;
  }

  public ActionRequestBuilder<K, V> name(String name)
  {
    _name = name;
    addParam(RestConstants.ACTION_PARAM, _name);
    return this;
  }

  public ActionRequestBuilder<K, V> id(K id)
  {
    _id = id;
    return this;
  }

  public ActionRequestBuilder<K, V> param(FieldDef<?> key, Object value)
  {
    _actionParams.put(key, value);
    return this;
  }

  @Override
  public ActionRequestBuilder<K, V> header(String key, String value)
  {
    super.header(key, value);
    return this;
  }

  @Override
  public ActionRequestBuilder<K, V> pathKey(String name, Object value)
  {
    super.pathKey(name, value);
    return this;
  }

  @Override
  public ActionRequest<V> build()
  {
    if (_name == null)
    {
      throw new IllegalStateException("name required to build action request");
    }
    
    UriBuilder b = UriBuilder.fromUri(bindPathKeys());
    if (_id != null)
    {
      b.path(_id.toString());
    }
    appendQueryParams(b);

    return new ActionRequest<V>(b.build(), new DynamicRecordTemplate(_name, _actionParams), _headers,  new ActionResponseDecoder<V>(_elementClass),
                                _resourceSpec);
  }
}