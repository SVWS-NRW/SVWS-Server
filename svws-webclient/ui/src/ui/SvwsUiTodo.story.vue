<template>
	<Story title="TODO Platzhalter" id="svws-ui-todo" icon="ri:box-3-line" :layout="{ type: 'single', iframe: false }" auto-props-disabled>
		<Variant title="Default" id="default">
			<svws-ui-todo :title="state.title" :hide-icon="state.hideIcon">
				Dieser Bereich ist noch in Entwicklung. Hier wird die Übersicht über XYZ implementiert.
			</svws-ui-todo>
			<div class="h-16" />
			<svws-ui-todo />
			<svws-ui-todo>
				TODO: custom text
			</svws-ui-todo>
			<template #source>
				{{ `
<svws-ui-todo />
        ` }}
			</template>
			<template #controls>
				<HstText v-model="state.title" title="title" />
				<HstCheckbox v-model="state.hideIcon" title="hideIcon" />
			</template>
		</Variant>
		<template #source>
			dsfsf
		</template>
	</Story>
</template>

<script setup lang="ts">
	import { reactive } from "vue";
	import SvwsUiTodo from "./SvwsUiTodo.vue";

	const state = reactive({
		title: 'Beispiel mit Titel',
		hideIcon: false,
	});
</script>

