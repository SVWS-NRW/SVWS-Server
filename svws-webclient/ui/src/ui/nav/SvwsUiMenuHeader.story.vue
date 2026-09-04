<template>
	<Story title="Sidebar/Menu Header" id="svws-ui-menu-header" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Default" id="Default">
			<div class="w-full max-w-20 mx-auto">
				<svws-ui-menu-header :user="state.user"
					:schule="state.schule"
					:schema="state.schema"
					@click="onClick" />
			</div>
		</Variant>
		<Variant title="Mit Hinweis" id="Hint">
			<div class="w-full max-w-20 mx-auto">
				<svws-ui-menu-header :user="state.user"
					:schule="state.schule"
					:schema="state.schema"
					:hint="state.hint"
					@click="onClick" />
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import { logEvent } from '@ui/stories/helper';
	import { reactive } from 'vue';
	import type { HintType } from './SvwsUiMenuHeader.vue';

	type HeaderType = {
		collapsed?: boolean;
		user?: string;
		schule?: string;
		schema?: string;
		isAdminClient?: boolean;
		hint?: HintType
	};

	const state = reactive<HeaderType>({
		user: 'Max Mustermann',
		schule: 'Muster-Gymnasium',
		schema: 'DBName',
		hint: { number: 5, type: 'highlight', text: '5 Hinweise zum Schüler liegen vor' },
	});

	function onClick(event: MouseEvent) {
		logEvent('click', event);
	}
</script>
