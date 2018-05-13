const area = [
<#list provinces as province>
{
  label: '${province.label}',
  options: [
    <#list province.options as city>
    { label: '${city.label}', value: '${city.value}' }<#if city_has_next>,</#if>
    </#list>
  ]
}<#if province_has_next>,</#if>
</#list>
]
