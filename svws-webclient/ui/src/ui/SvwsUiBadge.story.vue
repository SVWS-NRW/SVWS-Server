<template>
	<Story title="Badge" id="svws-ui-badge" :layout="{type: 'grid', width: '45%'}" icon="ri:shield-line">
		<Variant v-for="variant in variants" :key="variant.id" :title="variant.title" :id="variant.id">
			<div class="p-4">
				<svws-ui-badge :type="variant.type" :size="variant.size">Badge</svws-ui-badge>
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	type BadgeType = 'primary' | 'success' | 'error' | 'highlight' | 'light';
	type BadgeSize = 'small' | 'normal' | 'medium' | 'big';

	interface Variant {
		title: string;
		id: string;
		type?: BadgeType;
		size?: BadgeSize;
	}

	const variants: Variant[] = [
		{ title: 'Primary', id: 'Primary', type: 'primary' },
		{ title: 'Success', id: 'Success', type: 'success' },
		{ title: 'Error', id: 'Error', type: 'error' },
		{ title: 'Highlight', id: 'Highlight', type: 'highlight' },
		{ title: 'Light', id: 'Light', type: 'light' },
		{ title: 'Small', id: 'Small', size: 'small' },
		{ title: 'Normal', id: 'Normal', size: 'normal' },
		{ title: 'Medium', id: 'Medium', size: 'medium' },
		{ title: 'Big', id: 'Big', size: 'big' },
	];
</script>
