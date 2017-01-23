// ===================================================================================================
//                           _  __     _ _
//                          | |/ /__ _| | |_ _  _ _ _ __ _
//                          | ' </ _` | |  _| || | '_/ _` |
//                          |_|\_\__,_|_|\__|\_,_|_| \__,_|
//
// This file is part of the Kaltura Collaborative Media Suite which allows users
// to do with audio, video, and animation what Wiki platfroms allow them to do with
// text.
//
// Copyright (C) 2006-2016  Kaltura Inc.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// @ignore
// ===================================================================================================
package com.kaltura.client.types;

import org.w3c.dom.Element;
import com.kaltura.client.KalturaParams;
import com.kaltura.client.KalturaApiException;
import com.kaltura.client.enums.KalturaHttpNotificationMethod;
import com.kaltura.client.enums.KalturaHttpNotificationAuthenticationMethod;
import com.kaltura.client.enums.KalturaHttpNotificationSslVersion;
import com.kaltura.client.enums.KalturaHttpNotificationCertificateType;
import com.kaltura.client.enums.KalturaHttpNotificationSslKeyType;
import java.util.ArrayList;
import com.kaltura.client.utils.ParseUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class was generated using exec.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */

@SuppressWarnings("serial")
public class KalturaHttpNotificationDispatchJobData extends KalturaEventNotificationDispatchJobData {
	/**  Remote server URL  */
    public String url;
	/**  Request method.  */
    public KalturaHttpNotificationMethod method;
	/**  Data to send.  */
    public String data;
	/**  The maximum number of seconds to allow cURL functions to execute.  */
    public int timeout = Integer.MIN_VALUE;
	/**  The number of seconds to wait while trying to connect.   Must be larger than
	  zero.  */
    public int connectTimeout = Integer.MIN_VALUE;
	/**  A username to use for the connection.  */
    public String username;
	/**  A password to use for the connection.  */
    public String password;
	/**  The HTTP authentication method to use.  */
    public KalturaHttpNotificationAuthenticationMethod authenticationMethod;
	/**  The SSL version (2 or 3) to use.   By default PHP will try to determine this
	  itself, although in some cases this must be set manually.  */
    public KalturaHttpNotificationSslVersion sslVersion;
	/**  SSL certificate to verify the peer with.  */
    public String sslCertificate;
	/**  The format of the certificate.  */
    public KalturaHttpNotificationCertificateType sslCertificateType;
	/**  The password required to use the certificate.  */
    public String sslCertificatePassword;
	/**  The identifier for the crypto engine of the private SSL key specified in ssl
	  key.  */
    public String sslEngine;
	/**  The identifier for the crypto engine used for asymmetric crypto operations.  */
    public String sslEngineDefault;
	/**  The key type of the private SSL key specified in ssl key - PEM / DER / ENG.  */
    public KalturaHttpNotificationSslKeyType sslKeyType;
	/**  Private SSL key.  */
    public String sslKey;
	/**  The secret password needed to use the private SSL key specified in ssl key.  */
    public String sslKeyPassword;
	/**  Adds a e-mail custom header  */
    public ArrayList<KalturaKeyValue> customHeaders;
	/**  The secret to sign the notification with  */
    public String signSecret;

    public KalturaHttpNotificationDispatchJobData() {
    }

    public KalturaHttpNotificationDispatchJobData(Element node) throws KalturaApiException {
        super(node);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node aNode = childNodes.item(i);
            String nodeName = aNode.getNodeName();
            String txt = aNode.getTextContent();
            if (nodeName.equals("url")) {
                this.url = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("method")) {
                this.method = KalturaHttpNotificationMethod.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("data")) {
                this.data = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("timeout")) {
                this.timeout = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("connectTimeout")) {
                this.connectTimeout = ParseUtils.parseInt(txt);
                continue;
            } else if (nodeName.equals("username")) {
                this.username = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("password")) {
                this.password = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("authenticationMethod")) {
                this.authenticationMethod = KalturaHttpNotificationAuthenticationMethod.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("sslVersion")) {
                this.sslVersion = KalturaHttpNotificationSslVersion.get(ParseUtils.parseInt(txt));
                continue;
            } else if (nodeName.equals("sslCertificate")) {
                this.sslCertificate = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sslCertificateType")) {
                this.sslCertificateType = KalturaHttpNotificationCertificateType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("sslCertificatePassword")) {
                this.sslCertificatePassword = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sslEngine")) {
                this.sslEngine = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sslEngineDefault")) {
                this.sslEngineDefault = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sslKeyType")) {
                this.sslKeyType = KalturaHttpNotificationSslKeyType.get(ParseUtils.parseString(txt));
                continue;
            } else if (nodeName.equals("sslKey")) {
                this.sslKey = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("sslKeyPassword")) {
                this.sslKeyPassword = ParseUtils.parseString(txt);
                continue;
            } else if (nodeName.equals("customHeaders")) {
                this.customHeaders = ParseUtils.parseArray(KalturaKeyValue.class, aNode);
                continue;
            } else if (nodeName.equals("signSecret")) {
                this.signSecret = ParseUtils.parseString(txt);
                continue;
            } 
        }
    }

    public KalturaParams toParams() throws KalturaApiException {
        KalturaParams kparams = super.toParams();
        kparams.add("objectType", "KalturaHttpNotificationDispatchJobData");
        kparams.add("url", this.url);
        kparams.add("method", this.method);
        kparams.add("data", this.data);
        kparams.add("timeout", this.timeout);
        kparams.add("connectTimeout", this.connectTimeout);
        kparams.add("username", this.username);
        kparams.add("password", this.password);
        kparams.add("authenticationMethod", this.authenticationMethod);
        kparams.add("sslVersion", this.sslVersion);
        kparams.add("sslCertificate", this.sslCertificate);
        kparams.add("sslCertificateType", this.sslCertificateType);
        kparams.add("sslCertificatePassword", this.sslCertificatePassword);
        kparams.add("sslEngine", this.sslEngine);
        kparams.add("sslEngineDefault", this.sslEngineDefault);
        kparams.add("sslKeyType", this.sslKeyType);
        kparams.add("sslKey", this.sslKey);
        kparams.add("sslKeyPassword", this.sslKeyPassword);
        kparams.add("customHeaders", this.customHeaders);
        kparams.add("signSecret", this.signSecret);
        return kparams;
    }

}

