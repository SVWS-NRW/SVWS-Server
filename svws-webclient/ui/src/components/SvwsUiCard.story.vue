<template>
	<Story title="Card" id="svws-ui-card" icon="ri:window-2-fill" :layout="{ type: 'single' }" auto-props-disabled>
		<Variant title="Collapsible" id="Collapsible" :responsive-disabled="false">
			<div class="p-4">
				<svws-ui-card :compact="state.compact" :collapsible="true" :collapse-icon-position="state.iconPosition"
					:icon="state.showIcon ? 'i-ri-settings-2-line' : undefined" :title="state.title" :subtitle="state.subtitle" :info="state.info"
					:content="state.content" :show-divider="state.showDivider" :footer="state.footer" />
			</div>
			<template #controls>
				<HstCheckbox v-model="state.compact" title="kompakt" />
				<div class="text-headline-sm pl-2">Header</div>
				<HstCheckbox v-model="state.showIcon" title="Icon" />
				<HstText v-model="state.title" title="Titel" />
				<HstText v-model="state.subtitle" title="Untertitel" />
				<HstText v-model="state.info" title="Info" />
				<HstRadio v-model="state.iconPosition" title="Collapse Position" :options="[
					{ label: 'rechts', value: 'right' },
					{ label: 'links', value: 'left' },
				]" />
				<div class="text-headline-sm pl-2">Body</div>
				<HstTextarea v-model="state.content" title="Inhalt" />
				<HstCheckbox v-model="state.showDivider" title="Trennlinie" />
				<HstTextarea v-model="state.footer" title="Footer" />
			</template>
		</Variant>
		<Variant title="Nicht Collapsible" id="NotCollapsible">
			<div class="p-4">
				<svws-ui-card :compact="state.compact" :collapsible="false" :collapse-icon-position="state.iconPosition"
					:icon="state.showIcon ? 'i-ri-settings-2-line' : undefined" :title="state.title" :subtitle="state.subtitle" :info="state.info"
					:content="state.content" :show-divider="state.showDivider" :footer="state.footer" />
			</div>
			<template #controls>
				<HstCheckbox v-model="state.compact" title="kompakt" />
				<div class="text-headline-sm pl-2">Header</div>
				<HstCheckbox v-model="state.showIcon" title="Icon" />
				<HstText v-model="state.title" title="Titel" />
				<HstText v-model="state.subtitle" title="Untertitel" />
				<HstText v-model="state.info" title="Info" />
				<div class="text-headline-sm pl-2">Body</div>
				<HstTextarea v-model="state.content" title="Inhalt" />
				<HstCheckbox v-model="state.showDivider" title="Trennlinie" />
				<HstTextarea v-model="state.footer" title="Footer" />
			</template>
		</Variant>
		<Variant title="Collapsible Buttons" id="CollapsibleButtons">
			<div class="p-4">
				<svws-ui-card :compact="state.compact" :initially-opened="true" :collapsible="true" :collapse-icon-position="state.iconPosition"
					:icon="state.showIcon ? 'i-ri-settings-2-line' : undefined" :title="state.title" :subtitle="state.subtitle" :info="state.info"
					:content="state.content" :show-divider="state.showDivider" :footer="state.footer" :button-mode="state.buttonMode"
					:button-container="state.buttonContainer" :button-position="state.buttonPosition" :button-orientation="state.buttonOrientation"
					:on-edit="state.showEdit ? state.onEdit : undefined" :on-save="state.showSave ? state.onSave : undefined"
					:on-delete="state.showDelete ? state.onDelete : undefined" :on-cancel="state.showCancel ? state.onCancel : undefined" />
			</div>
			<template #controls>
				<HstCheckbox v-model="state.compact" title="kompakt" />
				<div class="text-headline-sm pl-2">Header</div>
				<HstCheckbox v-model="state.showIcon" title="Icon" />
				<HstText v-model="state.title" title="Titel" />
				<HstText v-model="state.subtitle" title="Untertitel" />
				<HstText v-model="state.info" title="Info" />
				<HstRadio v-model="state.iconPosition" title="Collapse Position" :options="[
					{ label: 'rechts', value: 'right' },
					{ label: 'links', value: 'left' },
				]" />
				<div class="text-headline-sm pl-2">Body</div>
				<HstTextarea v-model="state.content" title="Inhalt" />
				<HstCheckbox v-model="state.showDivider" title="Trennlinie" />
				<HstTextarea v-model="state.footer" title="Footer" />
				<div class="text-headline-sm pl-2">Buttons</div>
				<HstRadio v-model="state.buttonMode" title="Modus" :options="[
					{ label: 'Icons', value: 'icon' },
					{ label: 'Texte', value: 'text' },
				]" />
				<HstRadio v-model="state.buttonContainer" title="Container" :options="[
					{ label: 'Content', value: 'content' },
					{ label: 'Footer', value: 'footer' },
				]" />
				<HstRadio v-model="state.buttonPosition" title="Position" :options="[
					{ label: 'Links', value: 'left' },
					{ label: 'Rechts', value: 'right' },
				]" />
				<HstRadio v-model="state.buttonOrientation" title="Orientierung" :options="[
					{ label: 'vertikal', value: 'vertical' },
					{ label: 'horizontal', value: 'horizontal' },
				]" />
				<div class="font-medium p-2">Anzeige</div>
				<HstCheckbox v-model="state.showSave" title="Speichern" />
				<HstCheckbox v-model="state.showCancel" title="Abbrechen" />
				<HstCheckbox v-model="state.showEdit" title="Bearbeiten" />
				<HstCheckbox v-model="state.showDelete" title="Löschen" />
			</template>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import { reactive } from 'vue';

	const state = reactive({
		compact: false,
		title: 'Card Title',
		subtitle: 'Card Subtitle',
		info: 'Info Text',
		content: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, ' +
			'sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum ' +
			'dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna ' +
			'aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus ' +
			'est Lorem ipsum dolor sit amet.',
		showIcon: true,
		iconPosition: 'right' as 'left' | 'right',
		footer: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed' +
			'diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren',
		showDivider: true,
		buttonMode: 'icon' as 'icon' | 'text',
		onEdit: () => alert("Bearbeiten"),
		onSave: () => alert("Speichern"),
		onDelete: () => alert("Löschen"),
		onCancel: () => alert("Abbrechen"),
		showEdit: true,
		showDelete: true,
		showCancel: false,
		showSave: false,
		buttonPosition: 'right' as 'left' | 'right',
		buttonContainer: 'content' as 'content' | 'footer',
		buttonOrientation: 'horizontal' as 'vertical' | 'horizontal',
	})

</script>
