import{c as t,a as o,o as s}from"./index-Ypxb2S-y.js";const r={class:"prose"},p="design-system",d="Installation",m="install",u="#ccc",g="carbon:bookmark",v={__name:"Install.story",setup(l,{expose:n}){return n({frontmatter:{group:"design-system",title:"Installation",id:"install",iconColor:"#ccc",icon:"carbon:bookmark"}}),(c,e)=>(s(),t("div",r,e[0]||(e[0]=[o(`<h1>Vue3 UI-Komponenten für das SVWS-NRW Projekt</h1><p>Um die UI-Komponenten in ein vue3-Projekt einzubinden:</p><pre><code class="language-shell">npm i @svws-nrw/svws-ui
</code></pre><p>Komponenten können anschließend einzeln importiert werden:</p><pre><code class="language-vue">&lt;script setup lang=&quot;ts&quot;&gt;
  import { SvwsUiAvatar } from &#39;@svws-nrw/svws-ui&#39;
&lt;/script&gt;

&lt;template&gt;
  &lt;SvwsUiAvatar/&gt;
&lt;/template&gt;
</code></pre><p>Stories werden mit Hilfe von Histoire erstellt:</p><pre><code class="language-shell">npm run dev
</code></pre><pre><code class="language-shell">npm run story:build
</code></pre>`,8)])))}};export{v as default,p as group,g as icon,u as iconColor,m as id,d as title};
