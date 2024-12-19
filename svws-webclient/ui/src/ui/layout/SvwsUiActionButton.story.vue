<template>
	<Story title="Action Button" id="svws-ui-action-button" icon="ri:cursor-line" auto-props-disabled>
		<Variant title="Default" id="Default" :source="source">
			<div class="p-4">
				<svws-ui-action-button :title="state.title" @click="state.isActive = !state.isActive" :description="state.description" :icon="state.icon" :is-active="state.isActive" :is-loading="state.isLoading" :action-function="() => console.log('Action')" :action-label="state.actionLabel" :action-disabled="state.actionDisabled"><p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Nisi, quisquam sunt. Repellat adipisci maiores dolore sed porro ducimus soluta eligendi delectus veritatis modi, dignissimos impedit numquam corporis! Expedita, veritatis dolor.</p></svws-ui-action-button>
				<svws-ui-action-button title="Another Action Button">Inhalt</svws-ui-action-button>
			</div>

			<template #controls>
				<HstText v-model="state.title" title="Title" />
				<HstText v-model="state.description" title="Description" />
				<HstText v-model="state.icon" title="Icon" />
				<div class="h-3" />
				<hr>
				<div class="p-2 mt-3">
					<div class="text-sm font-bold mb-1">"Ausführen"-Button</div>
					<div>Der Button erscheint automatisch, wenn eine <code>actionFunction</code> übergeben wird.</div>
				</div>
				<HstText v-model="state.actionLabel" title="actionLabel" />
				<HstCheckbox v-model="state.actionDisabled" title="actionDisabled" />
				<HstCheckbox v-model="state.isLoading" title="isLoading" />
			</template>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import { reactive } from 'vue';

	const state = reactive({
		title: 'Title',
		description: 'Description',
		icon: 'i-ri-archive-line',
		isLoading: false,
		isActive: true,
		actionLabel: '',
		actionDisabled: false,
	})

	const source = `<svws-ui-action-button 
	title="Title" 
	description="Description"
	is-loading="isLoading().value"
	action-function="() => console.log('Action')">
		<div>Optional Input-Felder oder andere Elemente</div>
</svws-ui-action-button>`;
</script>
