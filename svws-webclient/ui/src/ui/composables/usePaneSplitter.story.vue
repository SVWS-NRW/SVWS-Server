
<template>
	<Story title="PaneSplitter" id="panesplitter" :source>
		<template #docs>
			<div class="prose min-w-full">
				Der PaneSplitter benötigt ein Div, dass die Panes umschließt und die Größe insgesamt vorgibt.
				<br>Innerhalb dieses Div gibt es drei weitere Div:
				<ol>
					<li>Das erste Feld, das in der Größe anpassbar ist</li>
					<li>Das zweite Feld, das in der Größe anpassbar ist</li>
					<li>Und der Dragger, der zwischen den beiden Feldern liegt und gezogen werden kann</li>
				</ol>
				Die beiden Felder bekommen als <pre>:style="thisStlye"</pre> und <pre>:style="thatStyle"</pre>,
				das äußere, umgebende Div ein <pre>ref="dragger"</pre>.
				Der Dragger selbst wird mit einem Listener ausgestattet: <pre>@mousedown.prevent="dragStart"</pre>
			</div>
		</template>
		<Variant title="Vertikal" id="vertical">
			Äußeres Div mit einem ref (schwarz)
			<div class="size-96 border flex flex-row" ref="dragger1">
				<div :style="leftStyle1" class="bg-ui-50">Linkes Div mit Style {{ leftStyle1 }}</div>
				<div class="w-2 border bg-ui-brand-hover cursor-ew-resize" @mousedown.prevent="dragStart1" />
				<div :style="rightStyle1" class="bg-ui-75">Rechtes Div mit Style {{ rightStyle1 }}</div>
			</div>
		</Variant>
		<Variant title="Horizontal" id="hrizontal">
			<div class="size-96 border flex flex-col" ref="dragger2">
				<div :style="upperStyle1">{{ upperStyle1 }}</div>
				<div class="h-2 w-full border bg-ui-brand-hover cursor-ns-resize" @mousedown.prevent="dragStart2" />
				<div :style="lowerStyle1">{{ lowerStyle1 }}</div>
			</div>
		</Variant>
		<Variant title="Nested" id="nested">
			<div class="size-96 border flex flex-row" ref="dragger1">
				<div class="flex flex-col border" :style="leftStyle1" ref="dragger2">
					<div :style="upperStyle1">{{ upperStyle1 }}</div>
					<div class="h-2 w-full border bg-ui-brand-hover cursor-ns-resize" @mousedown.prevent="dragStart2" />
					<div :style="lowerStyle1">{{ lowerStyle1 }}</div>
				</div>
				<div class="w-2 border bg-ui-brand-hover cursor-ew-resize" @mousedown.prevent="dragStart1" />
				<div :style="rightStyle1">{{ rightStyle1 }}</div>
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import { onUnmounted, reactive } from 'vue';
	import type { PaneSplitterConfig } from './usePaneSplitter';
	import { usePaneSplitter } from './usePaneSplitter';

	const configV = reactive<PaneSplitterConfig>({minSplit: 20, maxSplit: 80, mode: 'vertical', defaultSplit: 50});
	const configH = reactive<PaneSplitterConfig>({minSplit: 20, maxSplit: 80, mode: 'horizontal', defaultSplit: 50});

	const { removeDragListeners, dragStart: dragStart1, thisStyle: leftStyle1, thatStyle: rightStyle1, dragger: dragger1 } = usePaneSplitter(configV);
	const { dragStart: dragStart2, thisStyle: upperStyle1, thatStyle: lowerStyle1, dragger: dragger2 } = usePaneSplitter(configH);

	onUnmounted(removeDragListeners);

	const source = `
Äußeres Div mit einem ref (schwarz)
<div class="size-96 border flex flex-row" ref="dragger1">
	<div :style="leftStyle1" class="bg-ui-50">Linkes Div mit Style</div>
	<div class="w-2 border bg-ui-brand-hover cursor-ew-resize" @mousedown.prevent="dragStart1" />
	<div :style="rightStyle1" class="bg-ui-75">Rechtes Div mit Style</div>
</div>

<script setup lang="ts">
	const configV = reactive<PaneSplitterConfig>({minSplit: 20, maxSplit: 80, mode: 'vertical', defaultSplit: 50});
	const configH = reactive<PaneSplitterConfig>({minSplit: 20, maxSplit: 80, mode: 'horizontal', defaultSplit: 50});

	const { removeDragListeners, dragStart: dragStart1, thisStyle: leftStyle1, thatStyle: rightStyle1, dragger: dragger1 } = usePaneSplitter(configV);
	const { dragStart: dragStart2, thisStyle: upperStyle1, thatStyle: lowerStyle1, dragger: dragger2 } = usePaneSplitter(configH);
< /script>
`;
</script>