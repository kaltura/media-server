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
package com.kaltura.client.enums;

/**
 * This class was generated using exec.php
 * against an XML schema provided by Kaltura.
 * 
 * MANUAL CHANGES TO THIS CLASS WILL BE OVERWRITTEN.
 */
public enum KalturaLanguage implements KalturaEnumAsString {
    AB ("Abkhazian"),
    AA ("Afar"),
    AF ("Afrikaans"),
    SQ ("Albanian"),
    AM ("Amharic"),
    AR ("Arabic"),
    HY ("Armenian"),
    AS_ ("Assamese"),
    AY ("Aymara"),
    AZ ("Azerbaijani"),
    BA ("Bashkir"),
    EU ("Basque"),
    BN ("Bengali (Bangla)"),
    DZ ("Bhutani"),
    BH ("Bihari"),
    BI ("Bislama"),
    BR ("Breton"),
    BG ("Bulgarian"),
    MY ("Burmese"),
    BE ("Byelorussian (Belarusian)"),
    KM ("Cambodian"),
    CA ("Catalan"),
    ZH ("Chinese"),
    CO ("Corsican"),
    HR ("Croatian"),
    CS ("Czech"),
    DA ("Danish"),
    NL ("Dutch"),
    EN ("English"),
    EN_US ("English (American)"),
    EN_GB ("English (British)"),
    EO ("Esperanto"),
    ET ("Estonian"),
    FO ("Faeroese"),
    FA ("Farsi"),
    FJ ("Fiji"),
    FI ("Finnish"),
    FR ("French"),
    FY ("Frisian"),
    GV ("Gaelic (Manx)"),
    GD ("Gaelic (Scottish)"),
    GL ("Galician"),
    KA ("Georgian"),
    DE ("German"),
    EL ("Greek"),
    KL ("Greenlandic"),
    GN ("Guarani"),
    GU ("Gujarati"),
    HA ("Hausa"),
    IW ("Hebrew"),
    HI ("Hindi"),
    HU ("Hungarian"),
    IS ("Icelandic"),
    IN ("Indonesian"),
    IA ("Interlingua"),
    IE ("Interlingue"),
    IU ("Inuktitut"),
    IK ("Inupiak"),
    GA ("Irish"),
    IT ("Italian"),
    JA ("Japanese"),
    JV ("Javanese"),
    KN ("Kannada"),
    KS ("Kashmiri"),
    KK ("Kazakh"),
    RW ("Kinyarwanda (Ruanda)"),
    KY ("Kirghiz"),
    RN ("Kirundi (Rundi)"),
    KO ("Korean"),
    KU ("Kurdish"),
    LO ("Laothian"),
    LA ("Latin"),
    LV ("Latvian (Lettish)"),
    LI ("Limburgish ( Limburger)"),
    LN ("Lingala"),
    LT ("Lithuanian"),
    MK ("Macedonian"),
    MG ("Malagasy"),
    MS ("Malay"),
    ML ("Malayalam"),
    MT ("Maltese"),
    MI ("Maori"),
    MR ("Marathi"),
    MO ("Moldavian"),
    MN ("Mongolian"),
    MU ("Multilingual"),
    NA ("Nauru"),
    NE ("Nepali"),
    NO ("Norwegian"),
    OC ("Occitan"),
    OR_ ("Oriya"),
    OM ("Oromo (Afan, Galla)"),
    PS ("Pashto (Pushto)"),
    PL ("Polish"),
    PT ("Portuguese"),
    PA ("Punjabi"),
    QU ("Quechua"),
    RM ("Rhaeto-Romance"),
    RO ("Romanian"),
    RU ("Russian"),
    SM ("Samoan"),
    SG ("Sangro"),
    SA ("Sanskrit"),
    SR ("Serbian"),
    SH ("Serbo-Croatian"),
    ST ("Sesotho"),
    TN ("Setswana"),
    SN ("Shona"),
    SD ("Sindhi"),
    SI ("Sinhalese"),
    SS ("Siswati"),
    SK ("Slovak"),
    SL ("Slovenian"),
    SO ("Somali"),
    ES ("Spanish"),
    SU ("Sundanese"),
    SW ("Swahili (Kiswahili)"),
    SV ("Swedish"),
    TL ("Tagalog"),
    TG ("Tajik"),
    TA ("Tamil"),
    TT ("Tatar"),
    TE ("Telugu"),
    TH ("Thai"),
    BO ("Tibetan"),
    TI ("Tigrinya"),
    TO ("Tonga"),
    TS ("Tsonga"),
    TR ("Turkish"),
    TK ("Turkmen"),
    TW ("Twi"),
    UG ("Uighur"),
    UK ("Ukrainian"),
    UN ("Undefined"),
    UR ("Urdu"),
    UZ ("Uzbek"),
    VI ("Vietnamese"),
    VO ("Volapuk"),
    CY ("Welsh"),
    WO ("Wolof"),
    XH ("Xhosa"),
    JI ("Yiddish"),
    YO ("Yoruba"),
    ZU ("Zulu");

    public String hashCode;

    KalturaLanguage(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public static KalturaLanguage get(String hashCode) {
        if (hashCode.equals("Abkhazian"))
        {
           return AB;
        }
        else 
        if (hashCode.equals("Afar"))
        {
           return AA;
        }
        else 
        if (hashCode.equals("Afrikaans"))
        {
           return AF;
        }
        else 
        if (hashCode.equals("Albanian"))
        {
           return SQ;
        }
        else 
        if (hashCode.equals("Amharic"))
        {
           return AM;
        }
        else 
        if (hashCode.equals("Arabic"))
        {
           return AR;
        }
        else 
        if (hashCode.equals("Armenian"))
        {
           return HY;
        }
        else 
        if (hashCode.equals("Assamese"))
        {
           return AS_;
        }
        else 
        if (hashCode.equals("Aymara"))
        {
           return AY;
        }
        else 
        if (hashCode.equals("Azerbaijani"))
        {
           return AZ;
        }
        else 
        if (hashCode.equals("Bashkir"))
        {
           return BA;
        }
        else 
        if (hashCode.equals("Basque"))
        {
           return EU;
        }
        else 
        if (hashCode.equals("Bengali (Bangla)"))
        {
           return BN;
        }
        else 
        if (hashCode.equals("Bhutani"))
        {
           return DZ;
        }
        else 
        if (hashCode.equals("Bihari"))
        {
           return BH;
        }
        else 
        if (hashCode.equals("Bislama"))
        {
           return BI;
        }
        else 
        if (hashCode.equals("Breton"))
        {
           return BR;
        }
        else 
        if (hashCode.equals("Bulgarian"))
        {
           return BG;
        }
        else 
        if (hashCode.equals("Burmese"))
        {
           return MY;
        }
        else 
        if (hashCode.equals("Byelorussian (Belarusian)"))
        {
           return BE;
        }
        else 
        if (hashCode.equals("Cambodian"))
        {
           return KM;
        }
        else 
        if (hashCode.equals("Catalan"))
        {
           return CA;
        }
        else 
        if (hashCode.equals("Chinese"))
        {
           return ZH;
        }
        else 
        if (hashCode.equals("Corsican"))
        {
           return CO;
        }
        else 
        if (hashCode.equals("Croatian"))
        {
           return HR;
        }
        else 
        if (hashCode.equals("Czech"))
        {
           return CS;
        }
        else 
        if (hashCode.equals("Danish"))
        {
           return DA;
        }
        else 
        if (hashCode.equals("Dutch"))
        {
           return NL;
        }
        else 
        if (hashCode.equals("English"))
        {
           return EN;
        }
        else 
        if (hashCode.equals("English (American)"))
        {
           return EN_US;
        }
        else 
        if (hashCode.equals("English (British)"))
        {
           return EN_GB;
        }
        else 
        if (hashCode.equals("Esperanto"))
        {
           return EO;
        }
        else 
        if (hashCode.equals("Estonian"))
        {
           return ET;
        }
        else 
        if (hashCode.equals("Faeroese"))
        {
           return FO;
        }
        else 
        if (hashCode.equals("Farsi"))
        {
           return FA;
        }
        else 
        if (hashCode.equals("Fiji"))
        {
           return FJ;
        }
        else 
        if (hashCode.equals("Finnish"))
        {
           return FI;
        }
        else 
        if (hashCode.equals("French"))
        {
           return FR;
        }
        else 
        if (hashCode.equals("Frisian"))
        {
           return FY;
        }
        else 
        if (hashCode.equals("Gaelic (Manx)"))
        {
           return GV;
        }
        else 
        if (hashCode.equals("Gaelic (Scottish)"))
        {
           return GD;
        }
        else 
        if (hashCode.equals("Galician"))
        {
           return GL;
        }
        else 
        if (hashCode.equals("Georgian"))
        {
           return KA;
        }
        else 
        if (hashCode.equals("German"))
        {
           return DE;
        }
        else 
        if (hashCode.equals("Greek"))
        {
           return EL;
        }
        else 
        if (hashCode.equals("Greenlandic"))
        {
           return KL;
        }
        else 
        if (hashCode.equals("Guarani"))
        {
           return GN;
        }
        else 
        if (hashCode.equals("Gujarati"))
        {
           return GU;
        }
        else 
        if (hashCode.equals("Hausa"))
        {
           return HA;
        }
        else 
        if (hashCode.equals("Hebrew"))
        {
           return IW;
        }
        else 
        if (hashCode.equals("Hindi"))
        {
           return HI;
        }
        else 
        if (hashCode.equals("Hungarian"))
        {
           return HU;
        }
        else 
        if (hashCode.equals("Icelandic"))
        {
           return IS;
        }
        else 
        if (hashCode.equals("Indonesian"))
        {
           return IN;
        }
        else 
        if (hashCode.equals("Interlingua"))
        {
           return IA;
        }
        else 
        if (hashCode.equals("Interlingue"))
        {
           return IE;
        }
        else 
        if (hashCode.equals("Inuktitut"))
        {
           return IU;
        }
        else 
        if (hashCode.equals("Inupiak"))
        {
           return IK;
        }
        else 
        if (hashCode.equals("Irish"))
        {
           return GA;
        }
        else 
        if (hashCode.equals("Italian"))
        {
           return IT;
        }
        else 
        if (hashCode.equals("Japanese"))
        {
           return JA;
        }
        else 
        if (hashCode.equals("Javanese"))
        {
           return JV;
        }
        else 
        if (hashCode.equals("Kannada"))
        {
           return KN;
        }
        else 
        if (hashCode.equals("Kashmiri"))
        {
           return KS;
        }
        else 
        if (hashCode.equals("Kazakh"))
        {
           return KK;
        }
        else 
        if (hashCode.equals("Kinyarwanda (Ruanda)"))
        {
           return RW;
        }
        else 
        if (hashCode.equals("Kirghiz"))
        {
           return KY;
        }
        else 
        if (hashCode.equals("Kirundi (Rundi)"))
        {
           return RN;
        }
        else 
        if (hashCode.equals("Korean"))
        {
           return KO;
        }
        else 
        if (hashCode.equals("Kurdish"))
        {
           return KU;
        }
        else 
        if (hashCode.equals("Laothian"))
        {
           return LO;
        }
        else 
        if (hashCode.equals("Latin"))
        {
           return LA;
        }
        else 
        if (hashCode.equals("Latvian (Lettish)"))
        {
           return LV;
        }
        else 
        if (hashCode.equals("Limburgish ( Limburger)"))
        {
           return LI;
        }
        else 
        if (hashCode.equals("Lingala"))
        {
           return LN;
        }
        else 
        if (hashCode.equals("Lithuanian"))
        {
           return LT;
        }
        else 
        if (hashCode.equals("Macedonian"))
        {
           return MK;
        }
        else 
        if (hashCode.equals("Malagasy"))
        {
           return MG;
        }
        else 
        if (hashCode.equals("Malay"))
        {
           return MS;
        }
        else 
        if (hashCode.equals("Malayalam"))
        {
           return ML;
        }
        else 
        if (hashCode.equals("Maltese"))
        {
           return MT;
        }
        else 
        if (hashCode.equals("Maori"))
        {
           return MI;
        }
        else 
        if (hashCode.equals("Marathi"))
        {
           return MR;
        }
        else 
        if (hashCode.equals("Moldavian"))
        {
           return MO;
        }
        else 
        if (hashCode.equals("Mongolian"))
        {
           return MN;
        }
        else 
        if (hashCode.equals("Multilingual"))
        {
           return MU;
        }
        else 
        if (hashCode.equals("Nauru"))
        {
           return NA;
        }
        else 
        if (hashCode.equals("Nepali"))
        {
           return NE;
        }
        else 
        if (hashCode.equals("Norwegian"))
        {
           return NO;
        }
        else 
        if (hashCode.equals("Occitan"))
        {
           return OC;
        }
        else 
        if (hashCode.equals("Oriya"))
        {
           return OR_;
        }
        else 
        if (hashCode.equals("Oromo (Afan, Galla)"))
        {
           return OM;
        }
        else 
        if (hashCode.equals("Pashto (Pushto)"))
        {
           return PS;
        }
        else 
        if (hashCode.equals("Polish"))
        {
           return PL;
        }
        else 
        if (hashCode.equals("Portuguese"))
        {
           return PT;
        }
        else 
        if (hashCode.equals("Punjabi"))
        {
           return PA;
        }
        else 
        if (hashCode.equals("Quechua"))
        {
           return QU;
        }
        else 
        if (hashCode.equals("Rhaeto-Romance"))
        {
           return RM;
        }
        else 
        if (hashCode.equals("Romanian"))
        {
           return RO;
        }
        else 
        if (hashCode.equals("Russian"))
        {
           return RU;
        }
        else 
        if (hashCode.equals("Samoan"))
        {
           return SM;
        }
        else 
        if (hashCode.equals("Sangro"))
        {
           return SG;
        }
        else 
        if (hashCode.equals("Sanskrit"))
        {
           return SA;
        }
        else 
        if (hashCode.equals("Serbian"))
        {
           return SR;
        }
        else 
        if (hashCode.equals("Serbo-Croatian"))
        {
           return SH;
        }
        else 
        if (hashCode.equals("Sesotho"))
        {
           return ST;
        }
        else 
        if (hashCode.equals("Setswana"))
        {
           return TN;
        }
        else 
        if (hashCode.equals("Shona"))
        {
           return SN;
        }
        else 
        if (hashCode.equals("Sindhi"))
        {
           return SD;
        }
        else 
        if (hashCode.equals("Sinhalese"))
        {
           return SI;
        }
        else 
        if (hashCode.equals("Siswati"))
        {
           return SS;
        }
        else 
        if (hashCode.equals("Slovak"))
        {
           return SK;
        }
        else 
        if (hashCode.equals("Slovenian"))
        {
           return SL;
        }
        else 
        if (hashCode.equals("Somali"))
        {
           return SO;
        }
        else 
        if (hashCode.equals("Spanish"))
        {
           return ES;
        }
        else 
        if (hashCode.equals("Sundanese"))
        {
           return SU;
        }
        else 
        if (hashCode.equals("Swahili (Kiswahili)"))
        {
           return SW;
        }
        else 
        if (hashCode.equals("Swedish"))
        {
           return SV;
        }
        else 
        if (hashCode.equals("Tagalog"))
        {
           return TL;
        }
        else 
        if (hashCode.equals("Tajik"))
        {
           return TG;
        }
        else 
        if (hashCode.equals("Tamil"))
        {
           return TA;
        }
        else 
        if (hashCode.equals("Tatar"))
        {
           return TT;
        }
        else 
        if (hashCode.equals("Telugu"))
        {
           return TE;
        }
        else 
        if (hashCode.equals("Thai"))
        {
           return TH;
        }
        else 
        if (hashCode.equals("Tibetan"))
        {
           return BO;
        }
        else 
        if (hashCode.equals("Tigrinya"))
        {
           return TI;
        }
        else 
        if (hashCode.equals("Tonga"))
        {
           return TO;
        }
        else 
        if (hashCode.equals("Tsonga"))
        {
           return TS;
        }
        else 
        if (hashCode.equals("Turkish"))
        {
           return TR;
        }
        else 
        if (hashCode.equals("Turkmen"))
        {
           return TK;
        }
        else 
        if (hashCode.equals("Twi"))
        {
           return TW;
        }
        else 
        if (hashCode.equals("Uighur"))
        {
           return UG;
        }
        else 
        if (hashCode.equals("Ukrainian"))
        {
           return UK;
        }
        else 
        if (hashCode.equals("Undefined"))
        {
           return UN;
        }
        else 
        if (hashCode.equals("Urdu"))
        {
           return UR;
        }
        else 
        if (hashCode.equals("Uzbek"))
        {
           return UZ;
        }
        else 
        if (hashCode.equals("Vietnamese"))
        {
           return VI;
        }
        else 
        if (hashCode.equals("Volapuk"))
        {
           return VO;
        }
        else 
        if (hashCode.equals("Welsh"))
        {
           return CY;
        }
        else 
        if (hashCode.equals("Wolof"))
        {
           return WO;
        }
        else 
        if (hashCode.equals("Xhosa"))
        {
           return XH;
        }
        else 
        if (hashCode.equals("Yiddish"))
        {
           return JI;
        }
        else 
        if (hashCode.equals("Yoruba"))
        {
           return YO;
        }
        else 
        if (hashCode.equals("Zulu"))
        {
           return ZU;
        }
        else 
        {
           return AB;
        }
    }
}
